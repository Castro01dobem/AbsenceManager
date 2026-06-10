package com.itb.inf2dm.absencemanager.services;

import com.itb.inf2dm.absencemanager.model.entity.Turma;
import com.itb.inf2dm.absencemanager.model.entity.Usuario;
import com.itb.inf2dm.absencemanager.model.repository.TurmaRepository;
import com.itb.inf2dm.absencemanager.model.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TurmaService {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Turma> findAll() {
        return turmaRepository.findAll();
    }

    public Turma findById(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turma nao encontrada com o id: " + id));
    }

    public Turma create(Turma turma) {
        turma.setId(null);
        turma.setProfessor(resolveProfessor(turma));
        turma.setDataCadastro(LocalDateTime.now());
        turma.setStatusTurma(turma.getStatusTurma() == null ? "ATIVA" : turma.getStatusTurma());
        return turmaRepository.save(turma);
    }

    public Turma update(Long id, Turma turma) {
        Turma existente = findById(id);
        Usuario professor = resolveProfessor(turma);
        validarTrocaDeProfessor(existente, professor);

        existente.setNome(turma.getNome());
        existente.setInstrumento(turma.getInstrumento());
        existente.setPeriodo(turma.getPeriodo());
        existente.setAno(turma.getAno());
        existente.setVagas(turma.getVagas());
        existente.setObs(turma.getObs());
        existente.setProfessor(professor);
        existente.setStatusTurma(turma.getStatusTurma());
        existente.setDataAtualizacao(LocalDateTime.now());
        return turmaRepository.save(existente);
    }

    public Turma designarProfessor(Long turmaId, Long professorId) {
        Turma turma = findById(turmaId);

        if (turma.getProfessor() != null && turma.getProfessor().getId() != null) {
            throw new IllegalStateException("Esta turma ja possui um professor designado.");
        }

        Usuario professor = usuarioRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor nao encontrado com o id: " + professorId));

        if (!"PROFESSOR".equals(professor.getNivelAcesso())) {
            throw new IllegalArgumentException("O usuario selecionado nao e professor.");
        }

        turma.setProfessor(professor);
        turma.setDataAtualizacao(LocalDateTime.now());
        return turmaRepository.save(turma);
    }

    public void delete(Long id) {
        turmaRepository.delete(findById(id));
    }

    private Usuario resolveProfessor(Turma turma) {
        if (turma.getProfessor() == null || turma.getProfessor().getId() == null) {
            return null;
        }

        Usuario professor = usuarioRepository.findById(turma.getProfessor().getId())
                .orElseThrow(() -> new RuntimeException("Professor nao encontrado com o id: "
                        + turma.getProfessor().getId()));

        if (!"PROFESSOR".equals(professor.getNivelAcesso())) {
            throw new IllegalArgumentException("O usuario selecionado nao e professor.");
        }

        return professor;
    }

    private void validarTrocaDeProfessor(Turma turma, Usuario novoProfessor) {
        if (turma.getProfessor() == null || turma.getProfessor().getId() == null
                || novoProfessor == null || novoProfessor.getId() == null) {
            return;
        }

        if (!turma.getProfessor().getId().equals(novoProfessor.getId())) {
            throw new IllegalStateException("Esta turma ja possui um professor designado.");
        }
    }
}
