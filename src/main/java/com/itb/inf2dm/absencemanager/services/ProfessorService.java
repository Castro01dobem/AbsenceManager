package com.itb.inf2dm.absencemanager.services;

import com.itb.inf2dm.absencemanager.dto.ChamadaDetalhesResponseDTO;
import com.itb.inf2dm.absencemanager.dto.CriarChamadaResponseDTO;
import com.itb.inf2dm.absencemanager.model.entity.Chamada;
import com.itb.inf2dm.absencemanager.model.entity.Turma;
import com.itb.inf2dm.absencemanager.model.entity.TurmaAluno;
import com.itb.inf2dm.absencemanager.model.entity.Usuario;
import com.itb.inf2dm.absencemanager.model.repository.ChamadaAlunoRepository;
import com.itb.inf2dm.absencemanager.model.repository.ChamadaRepository;
import com.itb.inf2dm.absencemanager.model.repository.TurmaAlunoRepository;
import com.itb.inf2dm.absencemanager.model.repository.TurmaRepository;
import com.itb.inf2dm.absencemanager.model.repository.UsuarioRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class ProfessorService {

    private static final String NIVEL_PROFESSOR = "PROFESSOR";
    private static final String STATUS_PRESENTE = "PRESENTE";
    private static final String STATUS_FALTA = "FALTA";
    private static final String HEADER_CURRENT_USERNAME = "X-Current-Username";

    private final UsuarioRepository usuarioRepository;
    private final TurmaRepository turmaRepository;
    private final TurmaAlunoRepository turmaAlunoRepository;
    private final ChamadaRepository chamadaRepository;
    private final ChamadaAlunoRepository chamadaAlunoRepository;
    private final ChamadaService chamadaService;

    public ProfessorService(UsuarioRepository usuarioRepository, TurmaRepository turmaRepository,
            TurmaAlunoRepository turmaAlunoRepository, ChamadaRepository chamadaRepository,
            ChamadaAlunoRepository chamadaAlunoRepository, ChamadaService chamadaService) {
        this.usuarioRepository = usuarioRepository;
        this.turmaRepository = turmaRepository;
        this.turmaAlunoRepository = turmaAlunoRepository;
        this.chamadaRepository = chamadaRepository;
        this.chamadaAlunoRepository = chamadaAlunoRepository;
        this.chamadaService = chamadaService;
    }

    public List<Turma> listarTurmas(Authentication authentication) {
        Usuario professor = getProfessor(authentication);
        return turmaRepository.findByProfessorId(professor.getId());
    }

    public Turma buscarTurma(Authentication authentication, Long turmaId) {
        Usuario professor = getProfessor(authentication);
        return getTurmaDoProfessor(professor.getId(), turmaId);
    }

    public List<TurmaAluno> listarAlunos(Authentication authentication, Long turmaId) {
        Turma turma = buscarTurma(authentication, turmaId);
        return turmaAlunoRepository.findByTurmaId(turma.getId()).stream()
                .filter(vinculo -> Boolean.TRUE.equals(vinculo.getStatus()))
                .toList();
    }

    public Map<String, Object> dashboard(Authentication authentication) {
        Usuario professor = getProfessor(authentication);
        List<Turma> turmas = turmaRepository.findByProfessorId(professor.getId());
        List<Long> turmaIds = turmas.stream().map(Turma::getId).toList();

        long totalAlunos = turmaIds.isEmpty()
                ? 0
                : turmaAlunoRepository.countDistinctByTurmaIdInAndStatusTrue(turmaIds);
        long chamadasFeitas = chamadaRepository.countByTurmaProfessorId(professor.getId());
        long presencas = chamadaAlunoRepository.countByProfessorIdAndStatus(professor.getId(), STATUS_PRESENTE);
        long faltas = chamadaAlunoRepository.countByProfessorIdAndStatus(professor.getId(), STATUS_FALTA);
        long registros = presencas + faltas;

        Map<String, Object> dados = new LinkedHashMap<>();
        dados.put("totalTurmas", turmas.size());
        dados.put("totalAlunos", totalAlunos);
        dados.put("chamadasFeitas", chamadasFeitas);
        dados.put("percentualPresenca", percentual(presencas, registros));
        dados.put("percentualFaltas", percentual(faltas, registros));
        dados.put("turmas", turmas);
        return dados;
    }

    public Map<String, Object> relatorios(Authentication authentication) {
        Usuario professor = getProfessor(authentication);
        List<Map<String, Object>> turmas = turmaRepository.findByProfessorId(professor.getId())
                .stream()
                .map(this::relatorioDaTurma)
                .toList();

        Map<String, Object> dados = dashboard(authentication);
        dados.put("turmas", turmas);
        return dados;
    }

    public Map<String, Object> relatorioTurma(Authentication authentication, Long turmaId) {
        Turma turma = buscarTurma(authentication, turmaId);
        return relatorioDaTurma(turma);
    }

    public CriarChamadaResponseDTO criarChamada(Authentication authentication, Long turmaId) {
        Turma turma = buscarTurma(authentication, turmaId);
        return chamadaService.criarChamada(turma.getId());
    }

    public List<ChamadaDetalhesResponseDTO> listarChamadas(Authentication authentication, Long turmaId) {
        Turma turma = buscarTurma(authentication, turmaId);
        return chamadaService.listarPorTurma(turma.getId());
    }

    public ChamadaDetalhesResponseDTO buscarChamada(Authentication authentication, Long chamadaId) {
        Chamada chamada = getChamadaDoProfessor(authentication, chamadaId);
        return chamadaService.buscarDetalhes(chamada.getTurma().getId(), chamada.getId());
    }

    public ChamadaDetalhesResponseDTO encerrarChamada(Authentication authentication, Long chamadaId) {
        Chamada chamada = getChamadaDoProfessor(authentication, chamadaId);
        return chamadaService.confirmarChamada(chamada.getTurma().getId(), chamada.getId());
    }

    private Map<String, Object> relatorioDaTurma(Turma turma) {
        long presentes = chamadaAlunoRepository.countByTurmaIdAndStatus(turma.getId(), STATUS_PRESENTE);
        long faltas = chamadaAlunoRepository.countByTurmaIdAndStatus(turma.getId(), STATUS_FALTA);
        long total = presentes + faltas;
        List<Map<String, Object>> alunos = turmaAlunoRepository.findByTurmaId(turma.getId())
                .stream()
                .filter(vinculo -> Boolean.TRUE.equals(vinculo.getStatus()))
                .map(vinculo -> {
                    Integer rm = vinculo.getAluno().getRm();
                    long alunoPresentes = chamadaAlunoRepository.countByTurmaIdAndAlunoRmAndStatus(
                            turma.getId(), rm, STATUS_PRESENTE);
                    long alunoFaltas = chamadaAlunoRepository.countByTurmaIdAndAlunoRmAndStatus(
                            turma.getId(), rm, STATUS_FALTA);
                    long alunoTotal = alunoPresentes + alunoFaltas;
                    Map<String, Object> aluno = new LinkedHashMap<>();
                    aluno.put("rm", rm);
                    aluno.put("nome", vinculo.getAluno().getNome());
                    aluno.put("email", vinculo.getAluno().getEmail());
                    aluno.put("presentes", alunoPresentes);
                    aluno.put("faltas", alunoFaltas);
                    aluno.put("percentualPresenca", percentual(alunoPresentes, alunoTotal));
                    return aluno;
                })
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

    private Chamada getChamadaDoProfessor(Authentication authentication, Long chamadaId) {
        Usuario professor = getProfessor(authentication);
        Chamada chamada = chamadaRepository.findById(chamadaId)
                .orElseThrow(() -> new RuntimeException("Chamada nao encontrada com o id: " + chamadaId));
        validarTurmaDoProfessor(professor.getId(), chamada.getTurma());
        return chamada;
    }

    private Turma getTurmaDoProfessor(Long professorId, Long turmaId) {
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma nao encontrada com o id: " + turmaId));
        validarTurmaDoProfessor(professorId, turma);
        return turma;
    }

    private void validarTurmaDoProfessor(Long professorId, Turma turma) {
        if (turma.getProfessor() == null || !professorId.equals(turma.getProfessor().getId())) {
            throw new SecurityException("Professor nao possui acesso a esta turma.");
        }
    }

    private Usuario getProfessor(Authentication authentication) {
        String username = resolveUsername(authentication);
        if (username == null || username.isBlank()) {
            throw new SecurityException("Usuario nao autenticado.");
        }

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado."));

        if (!NIVEL_PROFESSOR.equals(usuario.getNivelAcesso())) {
            throw new SecurityException("Acesso permitido apenas para professores.");
        }

        return usuario;
    }

    private String resolveUsername(Authentication authentication) {
        if (authentication != null && authentication.getName() != null) {
            return authentication.getName();
        }

        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            return attributes.getRequest().getHeader(HEADER_CURRENT_USERNAME);
        }

        return null;
    }

    private double percentual(long parte, long total) {
        if (total <= 0) {
            return 0;
        }
        return Math.round((parte * 10000.0) / total) / 100.0;
    }
}
