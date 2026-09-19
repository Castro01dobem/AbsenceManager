package com.itb.inf2dm.absencemanager.services;

import com.itb.inf2dm.absencemanager.model.entity.Aluno;
import com.itb.inf2dm.absencemanager.model.entity.Turma;
import com.itb.inf2dm.absencemanager.model.entity.TurmaAluno;
import com.itb.inf2dm.absencemanager.model.entity.Usuario;
import com.itb.inf2dm.absencemanager.model.repository.AlunoRepository;
import com.itb.inf2dm.absencemanager.model.repository.ChamadaAlunoRepository;
import com.itb.inf2dm.absencemanager.model.repository.ChamadaRepository;
import com.itb.inf2dm.absencemanager.model.repository.TurmaAlunoRepository;
import com.itb.inf2dm.absencemanager.model.repository.TurmaRepository;
import com.itb.inf2dm.absencemanager.model.repository.UsuarioRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Relatorios administrativos calculados a partir dos dados reais de
 * Turma / Chamada / ChamadaAluno / Aluno (mesma fonte usada pelos
 * relatorios do professor), sem depender de nenhuma tabela nova.
 */
@Service
public class RelatorioService {

    private static final String STATUS_PRESENTE = "PRESENTE";
    private static final String STATUS_FALTA = "FALTA";
    private static final String STATUS_ALUNO_ATIVO = "ATIVO";
    private static final String NIVEL_PROFESSOR = "PROFESSOR";
    private static final double LIMITE_CRITICO_PADRAO = 75.0;

    private final TurmaRepository turmaRepository;
    private final TurmaAlunoRepository turmaAlunoRepository;
    private final AlunoRepository alunoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ChamadaRepository chamadaRepository;
    private final ChamadaAlunoRepository chamadaAlunoRepository;

    public RelatorioService(TurmaRepository turmaRepository, TurmaAlunoRepository turmaAlunoRepository,
            AlunoRepository alunoRepository, UsuarioRepository usuarioRepository,
            ChamadaRepository chamadaRepository, ChamadaAlunoRepository chamadaAlunoRepository) {
        this.turmaRepository = turmaRepository;
        this.turmaAlunoRepository = turmaAlunoRepository;
        this.alunoRepository = alunoRepository;
        this.usuarioRepository = usuarioRepository;
        this.chamadaRepository = chamadaRepository;
        this.chamadaAlunoRepository = chamadaAlunoRepository;
    }

    public Map<String, Object> geral() {
        long totalTurmas = turmaRepository.count();
        long totalAlunos = alunoRepository.findByStatusAluno(STATUS_ALUNO_ATIVO).size();
        long totalProfessores = usuarioRepository.findByNivelAcesso(NIVEL_PROFESSOR).size();
        long totalChamadas = chamadaRepository.count();
        long presencas = chamadaAlunoRepository.countByStatus(STATUS_PRESENTE);
        long faltas = chamadaAlunoRepository.countByStatus(STATUS_FALTA);
        long registros = presencas + faltas;

        Map<String, Object> dados = new LinkedHashMap<>();
        dados.put("totalTurmas", totalTurmas);
        dados.put("totalAlunos", totalAlunos);
        dados.put("totalProfessores", totalProfessores);
        dados.put("totalChamadas", totalChamadas);
        dados.put("totalPresencas", presencas);
        dados.put("totalFaltas", faltas);
        dados.put("percentualPresenca", percentual(presencas, registros));
        dados.put("percentualFaltas", percentual(faltas, registros));
        return dados;
    }

    public List<Map<String, Object>> listarTurmas() {
        return turmaRepository.findAll().stream()
                .map(this::relatorioDaTurma)
                .toList();
    }

    public Map<String, Object> turma(Long turmaId) {
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma nao encontrada com o id: " + turmaId));
        return relatorioDaTurma(turma);
    }

    public List<Map<String, Object>> listarProfessores() {
        return usuarioRepository.findByNivelAcesso(NIVEL_PROFESSOR).stream()
                .map(this::relatorioDoProfessor)
                .toList();
    }

    public List<Map<String, Object>> alunosCriticos(Double limitePercentual) {
        double limite = limitePercentual == null ? LIMITE_CRITICO_PADRAO : limitePercentual;

        return alunoRepository.findAll().stream()
                .map(this::frequenciaDoAluno)
                .filter(dados -> ((Number) dados.get("totalChamadas")).longValue() > 0)
                .filter(dados -> ((Number) dados.get("percentualPresenca")).doubleValue() < limite)
                .toList();
    }

    private Map<String, Object> relatorioDaTurma(Turma turma) {
        long presentes = chamadaAlunoRepository.countByTurmaIdAndStatus(turma.getId(), STATUS_PRESENTE);
        long faltas = chamadaAlunoRepository.countByTurmaIdAndStatus(turma.getId(), STATUS_FALTA);
        long total = presentes + faltas;

        List<Map<String, Object>> alunos = turmaAlunoRepository.findByTurmaId(turma.getId()).stream()
                .filter(vinculo -> Boolean.TRUE.equals(vinculo.getStatus()))
                .map(vinculo -> alunoDaTurma(turma.getId(), vinculo))
                .toList();

        Map<String, Object> dados = new LinkedHashMap<>();
        dados.put("turma", turma);
        dados.put("totalChamadas", chamadaRepository.findByTurma_IdOrderByDataGeracaoDesc(turma.getId()).size());
        dados.put("presentes", presentes);
        dados.put("faltas", faltas);
        dados.put("percentualPresenca", percentual(presentes, total));
        dados.put("percentualFaltas", percentual(faltas, total));
        dados.put("alunos", alunos);
        return dados;
    }

    private Map<String, Object> alunoDaTurma(Long turmaId, TurmaAluno vinculo) {
        Integer rm = vinculo.getAluno().getRm();
        long presentes = chamadaAlunoRepository.countByTurmaIdAndAlunoRmAndStatus(turmaId, rm, STATUS_PRESENTE);
        long faltas = chamadaAlunoRepository.countByTurmaIdAndAlunoRmAndStatus(turmaId, rm, STATUS_FALTA);
        long total = presentes + faltas;

        Map<String, Object> aluno = new LinkedHashMap<>();
        aluno.put("rm", rm);
        aluno.put("nome", vinculo.getAluno().getNome());
        aluno.put("email", vinculo.getAluno().getEmail());
        aluno.put("presentes", presentes);
        aluno.put("faltas", faltas);
        aluno.put("percentualPresenca", percentual(presentes, total));
        return aluno;
    }

    private Map<String, Object> relatorioDoProfessor(Usuario professor) {
        List<Turma> turmas = turmaRepository.findByProfessorId(professor.getId());
        long presentes = chamadaAlunoRepository.countByProfessorIdAndStatus(professor.getId(), STATUS_PRESENTE);
        long faltas = chamadaAlunoRepository.countByProfessorIdAndStatus(professor.getId(), STATUS_FALTA);
        long total = presentes + faltas;

        Map<String, Object> dados = new LinkedHashMap<>();
        dados.put("professorId", professor.getId());
        dados.put("nome", professor.getNome());
        dados.put("username", professor.getUsername());
        dados.put("totalTurmas", turmas.size());
        dados.put("presentes", presentes);
        dados.put("faltas", faltas);
        dados.put("percentualPresenca", percentual(presentes, total));
        dados.put("percentualFaltas", percentual(faltas, total));
        return dados;
    }

    private Map<String, Object> frequenciaDoAluno(Aluno aluno) {
        long presentes = chamadaAlunoRepository.countByAlunoRmAndStatus(aluno.getRm(), STATUS_PRESENTE);
        long faltas = chamadaAlunoRepository.countByAlunoRmAndStatus(aluno.getRm(), STATUS_FALTA);
        long total = presentes + faltas;

        Map<String, Object> dados = new LinkedHashMap<>();
        dados.put("rm", aluno.getRm());
        dados.put("nome", aluno.getNome());
        dados.put("email", aluno.getEmail());
        dados.put("presentes", presentes);
        dados.put("faltas", faltas);
        dados.put("totalChamadas", total);
        dados.put("percentualPresenca", percentual(presentes, total));
        return dados;
    }

    private double percentual(long parte, long total) {
        if (total <= 0) {
            return 0;
        }
        return Math.round((parte * 10000.0) / total) / 100.0;
    }
}
