package com.itb.inf2dm.absencemanager.services;

import com.itb.inf2dm.absencemanager.dto.AlunoChamadaDTO;
import com.itb.inf2dm.absencemanager.dto.ChamadaDetalhesResponseDTO;
import com.itb.inf2dm.absencemanager.dto.ConfirmarPresencaRequestDTO;
import com.itb.inf2dm.absencemanager.dto.CriarChamadaResponseDTO;
import com.itb.inf2dm.absencemanager.model.entity.Aluno;
import com.itb.inf2dm.absencemanager.model.entity.Chamada;
import com.itb.inf2dm.absencemanager.model.entity.ChamadaAluno;
import com.itb.inf2dm.absencemanager.model.entity.Turma;
import com.itb.inf2dm.absencemanager.model.entity.TurmaAluno;
import com.itb.inf2dm.absencemanager.model.repository.AlunoRepository;
import com.itb.inf2dm.absencemanager.model.repository.ChamadaAlunoRepository;
import com.itb.inf2dm.absencemanager.model.repository.ChamadaRepository;
import com.itb.inf2dm.absencemanager.model.repository.TurmaAlunoRepository;
import com.itb.inf2dm.absencemanager.model.repository.TurmaRepository;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChamadaService {

    private static final String STATUS_ATIVA = "ATIVA";
    private static final String STATUS_ENCERRADA = "ENCERRADA";
    private static final String STATUS_PRESENTE = "PRESENTE";
    private static final String STATUS_FALTA = "FALTA";
    private static final int DURACAO_HORAS = 2;

    private final TurmaRepository turmaRepository;
    private final TurmaAlunoRepository turmaAlunoRepository;
    private final AlunoRepository alunoRepository;
    private final ChamadaRepository chamadaRepository;
    private final ChamadaAlunoRepository chamadaAlunoRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public ChamadaService(TurmaRepository turmaRepository, TurmaAlunoRepository turmaAlunoRepository,
            AlunoRepository alunoRepository, ChamadaRepository chamadaRepository,
            ChamadaAlunoRepository chamadaAlunoRepository) {
        this.turmaRepository = turmaRepository;
        this.turmaAlunoRepository = turmaAlunoRepository;
        this.alunoRepository = alunoRepository;
        this.chamadaRepository = chamadaRepository;
        this.chamadaAlunoRepository = chamadaAlunoRepository;
    }

    @Transactional
    public CriarChamadaResponseDTO criarChamada(Long turmaId) {
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma nao encontrada com o id: " + turmaId));

        List<TurmaAluno> vinculos = turmaAlunoRepository.findByTurmaId(turmaId)
                .stream()
                .filter(vinculo -> Boolean.TRUE.equals(vinculo.getStatus()))
                .toList();

        if (vinculos.isEmpty()) {
            throw new IllegalArgumentException("Nao ha alunos ativos vinculados a esta turma.");
        }

        LocalDateTime agora = LocalDateTime.now();
        String token = gerarTokenUnico();
        Chamada chamada = new Chamada();
        chamada.setTurma(turma);
        chamada.setToken(token);
        chamada.setQrCodePayload(montarPayload(token));
        chamada.setDataGeracao(agora);
        chamada.setDataExpiracao(agora.plusHours(DURACAO_HORAS));
        chamada.setStatus(STATUS_ATIVA);
        Chamada chamadaSalva = chamadaRepository.save(chamada);

        List<ChamadaAluno> registros = vinculos.stream()
                .map(vinculo -> criarRegistroFalta(chamadaSalva, vinculo.getAluno()))
                .toList();
        chamadaAlunoRepository.saveAll(registros);

        ChamadaDetalhesResponseDTO detalhes = montarDetalhes(chamadaSalva);
        return new CriarChamadaResponseDTO(
                detalhes.getId(),
                detalhes.getTurmaId(),
                detalhes.getToken(),
                detalhes.getQrCodePayload(),
                detalhes.getDataGeracao(),
                detalhes.getDataExpiracao(),
                detalhes.getStatus(),
                detalhes.getAlunos()
        );
    }

    @Transactional
    public ChamadaDetalhesResponseDTO confirmarPresenca(ConfirmarPresencaRequestDTO request) {
        Chamada chamada = chamadaRepository.findByToken(request.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Chamada nao encontrada."));

        validarChamadaAtiva(chamada);

        String email = request.getEmail() == null ? null : request.getEmail().trim();
        Aluno aluno = alunoRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email nao encontrado."));

        if (!turmaAlunoRepository.existsByTurmaIdAndAlunoRm(chamada.getTurma().getId(), aluno.getRm())) {
            throw new IllegalArgumentException("Aluno nao pertence a esta turma.");
        }

        ChamadaAluno chamadaAluno = chamadaAlunoRepository
                .findByChamadaIdAndAlunoRm(chamada.getId(), aluno.getRm())
                .orElseThrow(() -> new IllegalArgumentException("Aluno nao possui registro nesta chamada."));

        if (!STATUS_PRESENTE.equals(chamadaAluno.getStatus())) {
            chamadaAluno.setStatus(STATUS_PRESENTE);
            chamadaAluno.setDataConfirmacao(LocalDateTime.now());
            chamadaAlunoRepository.save(chamadaAluno);
        }

        return montarDetalhes(chamada);
    }

    public ChamadaDetalhesResponseDTO buscarDetalhes(Long turmaId, Long chamadaId) {
        Chamada chamada = chamadaRepository.findById(chamadaId)
                .orElseThrow(() -> new RuntimeException("Chamada nao encontrada com o id: " + chamadaId));

        if (!chamada.getTurma().getId().equals(turmaId)) {
            throw new IllegalArgumentException("Chamada nao pertence a esta turma.");
        }

        return montarDetalhes(chamada);
    }

    public List<ChamadaDetalhesResponseDTO> listarRecentes(Long turmaId) {
        if (!turmaRepository.existsById(turmaId)) {
            throw new RuntimeException("Turma nao encontrada com o id: " + turmaId);
        }

        return chamadaRepository.findTop5ByTurma_IdOrderByDataGeracaoDesc(turmaId)
                .stream()
                .map(this::montarDetalhes)
                .toList();
    }

    public List<ChamadaDetalhesResponseDTO> listarPorTurma(Long turmaId) {
        if (!turmaRepository.existsById(turmaId)) {
            throw new RuntimeException("Turma nao encontrada com o id: " + turmaId);
        }

        return chamadaRepository.findByTurma_IdOrderByDataGeracaoDesc(turmaId)
                .stream()
                .map(this::montarDetalhes)
                .toList();
    }

    @Transactional
    public ChamadaDetalhesResponseDTO confirmarChamada(Long turmaId, Long chamadaId) {
        Chamada chamada = chamadaRepository.findById(chamadaId)
                .orElseThrow(() -> new RuntimeException("Chamada nao encontrada com o id: " + chamadaId));

        if (!chamada.getTurma().getId().equals(turmaId)) {
            throw new IllegalArgumentException("Chamada nao pertence a esta turma.");
        }

        if (!STATUS_ENCERRADA.equals(chamada.getStatus())) {
            chamada.setStatus(STATUS_ENCERRADA);
            chamadaRepository.save(chamada);
        }

        return montarDetalhes(chamada);
    }

    private void validarChamadaAtiva(Chamada chamada) {
        if (!STATUS_ATIVA.equals(chamada.getStatus())) {
            throw new IllegalArgumentException("Chamada inativa.");
        }

        if (chamada.getDataExpiracao().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Chamada expirada.");
        }
    }

    private ChamadaAluno criarRegistroFalta(Chamada chamada, Aluno aluno) {
        ChamadaAluno registro = new ChamadaAluno();
        registro.setChamada(chamada);
        registro.setAluno(aluno);
        registro.setStatus(STATUS_FALTA);
        return registro;
    }

    private ChamadaDetalhesResponseDTO montarDetalhes(Chamada chamada) {
        List<AlunoChamadaDTO> alunos = chamadaAlunoRepository.findByChamadaId(chamada.getId())
                .stream()
                .map(this::toAlunoChamadaDTO)
                .toList();

        long totalPresentes = alunos.stream()
                .filter(aluno -> STATUS_PRESENTE.equals(aluno.getStatusPresenca()))
                .count();
        long totalFaltas = alunos.stream()
                .filter(aluno -> STATUS_FALTA.equals(aluno.getStatusPresenca()))
                .count();

        return new ChamadaDetalhesResponseDTO(
                chamada.getId(),
                chamada.getTurma().getId(),
                chamada.getTurma().getNome(),
                chamada.getToken(),
                getQrCodePayload(chamada),
                chamada.getDataGeracao(),
                chamada.getDataExpiracao(),
                chamada.getStatus(),
                totalPresentes,
                totalFaltas,
                alunos
        );
    }

    private AlunoChamadaDTO toAlunoChamadaDTO(ChamadaAluno chamadaAluno) {
        Aluno aluno = chamadaAluno.getAluno();
        return new AlunoChamadaDTO(
                aluno.getRm(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getTelefone(),
                aluno.getStatusAluno(),
                chamadaAluno.getStatus(),
                chamadaAluno.getDataConfirmacao()
        );
    }

    private String montarPayload(String token) {
        return "absencemanager://confirmar-presenca?token=" + token;
    }

    private String getQrCodePayload(Chamada chamada) {
        if (chamada.getQrCodePayload() != null && !chamada.getQrCodePayload().isBlank()) {
            return chamada.getQrCodePayload();
        }

        return montarPayload(chamada.getToken());
    }

    private String gerarTokenUnico() {
        String token;
        do {
            byte[] bytes = new byte[32];
            secureRandom.nextBytes(bytes);
            token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        } while (chamadaRepository.existsByToken(token));

        return token;
    }
}
