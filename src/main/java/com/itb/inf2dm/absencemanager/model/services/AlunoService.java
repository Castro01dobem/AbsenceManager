package com.itb.inf2dm.absencemanager.model.services;

import com.itb.inf2dm.absencemanager.model.entity.Aluno;
import com.itb.inf2dm.absencemanager.model.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// @Autowired: Injeção de Dependência, ou seja, a referida classe exige o objeto declarado abaixo.

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;


    // Listar todos os produtos
    public List<Aluno> findAll() {
        return alunoRepository.findAll();
    }

    // Salvar Produto
    public Aluno save(Aluno aluno) {
        aluno.setStatusAluno("Ativo");
        return alunoRepository.save(aluno);
    }

    // Listar Produto por Id

    public Aluno findById(int id) {
        return alunoRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Aluno não encontrado com o id:" + id));
    }

    // Atualizar Produto

    public Aluno update(int id, Aluno aluno) {

        Aluno alunoExistente = findById(id);
        alunoExistente.setNome(aluno.getNome());
        alunoExistente.setId(aluno.getId());
        alunoExistente.setRm(aluno.getRm());
        alunoExistente.setDataNascimento(aluno.getDataNascimento());
        alunoExistente.setSexo(aluno.getSexo());
        alunoExistente.setStatusAluno(aluno.getStatusAluno());
        alunoExistente.setUsuarioId(aluno.getUsuarioId());
        alunoExistente.setTelefone(aluno.getTelefone());
        return alunoRepository.save(alunoExistente);
    }


    // Excluir Aluno
    public void delete(int id) {
        Aluno alunoExistente = findById(id);
        alunoRepository.delete(alunoExistente);
    }
}

