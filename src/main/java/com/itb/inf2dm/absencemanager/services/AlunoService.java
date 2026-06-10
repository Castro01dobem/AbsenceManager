package com.itb.inf2dm.absencemanager.services;

import com.itb.inf2dm.absencemanager.model.entity.Aluno;
import com.itb.inf2dm.absencemanager.model.repository.AlunoRepository;
import com.itb.inf2dm.absencemanager.model.repository.PresencaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private PresencaRepository presencaRepository;

    public List<Aluno> findAll() {
        return alunoRepository.findAll();
    }

    public Aluno save(Aluno aluno) {
        aluno.setRm(null);
        aluno.setStatusAluno("ATIVO");
        return alunoRepository.save(aluno);
    }

    public Aluno findById(int rm) {
        return alunoRepository.findById(rm)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com o rm: " + rm));
    }

    public Aluno update(int rm, Aluno aluno) {
        Aluno existente = findById(rm);
        existente.setNome(aluno.getNome());
        existente.setDataNascimento(aluno.getDataNascimento());
        existente.setSexo(aluno.getSexo());
        existente.setCpf(aluno.getCpf());
        existente.setTelefone(aluno.getTelefone());
        existente.setUsuario(aluno.getUsuario());
        existente.setStatusAluno(aluno.getStatusAluno());
        return alunoRepository.save(existente);
    }

    public void delete(int rm) {
        Aluno aluno = findById(rm);

        if (presencaRepository.existsByTurmaAlunoAlunoRm(rm)) {
            throw new IllegalStateException("Nao e possivel excluir este aluno porque ele possui registros de presenca.");
        }

        try {
            alunoRepository.delete(aluno);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Nao e possivel excluir este aluno porque ele possui registros relacionados.");
        }
    }

    public Aluno inativar(int rm) {
        Aluno aluno = findById(rm);

        if (aluno.getUsuario() != null) {
            aluno.getUsuario().setStatusUsuario("INATIVO");
        }
        aluno.setStatusAluno("INATIVO");

        return alunoRepository.save(aluno);
    }
}

