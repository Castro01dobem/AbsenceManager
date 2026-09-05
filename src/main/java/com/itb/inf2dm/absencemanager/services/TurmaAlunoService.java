package com.itb.inf2dm.absencemanager.services;

import com.itb.inf2dm.absencemanager.model.entity.Aluno;
import com.itb.inf2dm.absencemanager.model.entity.Turma;
import com.itb.inf2dm.absencemanager.model.entity.TurmaAluno;
import com.itb.inf2dm.absencemanager.model.repository.AlunoRepository;
import com.itb.inf2dm.absencemanager.model.repository.TurmaAlunoRepository;
import com.itb.inf2dm.absencemanager.model.repository.TurmaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TurmaAlunoService {

    @Autowired
    private TurmaAlunoRepository turmaAlunoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    public List<TurmaAluno> findAll() {
        return turmaAlunoRepository.findAll();
    }

    public List<TurmaAluno> findByTurmaId(Long turmaId) {
        return turmaAlunoRepository.findByTurmaId(turmaId).stream()
                .filter(vinculo -> Boolean.TRUE.equals(vinculo.getStatus()))
                .toList();
    }

    public List<TurmaAluno> findByAlunoRm(Integer alunoRm) {
        return turmaAlunoRepository.findByAlunoRm(alunoRm).stream()
                .filter(vinculo -> Boolean.TRUE.equals(vinculo.getStatus()))
                .toList();
    }

    public TurmaAluno findById(Long id) {
        return turmaAlunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vinculo de turma e aluno nao encontrado com o id: " + id));
    }

    @Transactional
    public TurmaAluno create(TurmaAluno turmaAluno) {
        Long turmaId = turmaAluno.getTurma() != null ? turmaAluno.getTurma().getId() : null;
        Integer alunoRm = turmaAluno.getAluno() != null ? turmaAluno.getAluno().getRm() : null;

        if (turmaId == null || alunoRm == null) {
            throw new IllegalArgumentException("Turma e aluno sao obrigatorios.");
        }

        if (turmaAlunoRepository.existsByTurmaIdAndAlunoRm(turmaId, alunoRm)) {
            throw new IllegalArgumentException("Este aluno ja esta vinculado a esta turma.");
        }

        if (!turmaAlunoRepository.findByAlunoRmAndStatusTrue(alunoRm).isEmpty()) {
            throw new IllegalArgumentException("Este aluno ja possui uma turma ativa.");
        }

        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RuntimeException("Turma nao encontrada com o id: " + turmaId));

        Aluno aluno = alunoRepository.findById(alunoRm)
                .orElseThrow(() -> new RuntimeException("Aluno nao encontrado com o rm: " + alunoRm));

        Boolean status = turmaAluno.getStatus() == null ? true : turmaAluno.getStatus();
        if (Boolean.TRUE.equals(status)) {
            Integer vagasDisponiveis = turma.getVagas() == null ? 0 : turma.getVagas();
            if (vagasDisponiveis <= 0) {
                throw new IllegalArgumentException("Esta turma nao possui vagas disponiveis.");
            }
            turma.setVagas(vagasDisponiveis - 1);
        }

        turmaAluno.setTurma(turma);
        turmaAluno.setAluno(aluno);
        turmaAluno.setStatus(status);

        TurmaAluno vinculo = turmaAlunoRepository.save(turmaAluno);
        turmaRepository.save(turma);
        return vinculo;
    }

    @Transactional
    public void delete(Long id) {
        TurmaAluno vinculo = findById(id);
        if (Boolean.TRUE.equals(vinculo.getStatus()) && vinculo.getTurma() != null) {
            Turma turma = vinculo.getTurma();
            turma.setVagas((turma.getVagas() == null ? 0 : turma.getVagas()) + 1);
            turmaRepository.save(turma);
        }
        turmaAlunoRepository.delete(vinculo);
    }
}
