package com.itb.inf2dm.absencemanager.model.services;

import com.itb.inf2dm.absencemanager.model.entity.AlunoTurma;
import com.itb.inf2dm.absencemanager.model.repository.AlunoTurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// @Autowired: Injeção de Dependência, ou seja, a referida classe exige o objeto declarado abaixo.

@Service
public class AlunoTurmaService {

    @Autowired
    private AlunoTurmaRepository alunoTurmaRepository;


    // Listar todos os produtos
    public List<AlunoTurma> findAll() {
        return alunoTurmaRepository.findAll();
    }

    // Salvar Produto
    public AlunoTurma save(AlunoTurma alunoTurma) {
        alunoTurma.setStatusAlunoTurma("Ativo");
        return alunoTurmaRepository.save(alunoTurma);
    }

    // Listar Produto por Id
    public AlunoTurma findById(int id) {
        return alunoTurmaRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Turma do aluno não encontrado com o id:" + id));
    }

    // Atualizar Produto
    public AlunoTurma update(int id, AlunoTurma alunoTurma) {

        AlunoTurma alunoTurmaExistente = findById(id);
        alunoTurmaExistente.setAluno_id(alunoTurma.getAluno_id());
        alunoTurmaExistente.setId(alunoTurma.getId());
        alunoTurmaExistente.setTurma_id(alunoTurma.getTurma_id());
        alunoTurmaExistente.setStatusAlunoTurma(alunoTurma.getStatusAlunoTurma());
        alunoTurmaExistente.setDataCadastro(alunoTurma.getDataCadastro());
        return alunoTurmaRepository.save(alunoTurmaExistente);
    }


    // Excluir Aluno
    public void delete(int id) {
        AlunoTurma alunoTurmaExistente = findById(id);
        alunoTurmaRepository.delete(alunoTurmaExistente);
    }
}
