package com.itb.inf2dm.absencemanager.services;

import com.itb.inf2dm.absencemanager.model.entity.Aluno;
import com.itb.inf2dm.absencemanager.model.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

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
        alunoRepository.delete(findById(rm));
    }
}
