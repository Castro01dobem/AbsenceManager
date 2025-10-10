package com.itb.inf2dm.absencemanager.model.services;

import com.itb.inf2dm.absencemanager.model.entity.Turma;
import com.itb.inf2dm.absencemanager.model.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// @Autowired : Injeção de Dependência, ou seja, a referida classe exige o objeto declarado abaixo.

@Service
public class TurmaService {

    @Autowired
    private TurmaRepository turmaRepository;

    // Listar todas as turmas

    public List<Turma> findAll() {
        return turmaRepository.findAll();
    }

    // Salvar Produto

    public Turma save(Turma turma) {
        turma.setNomeTurma("");
        return turmaRepository.save(turma);
    }

    // Listar Turma por Id

    public Turma findById(int id) {
        return turmaRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Turma não encontrada com o id: " + id));
    }


    // Atualizar Turma

    public Turma update(int id, Turma turma) {

        Turma turmaExistente = findById(id);
        turmaExistente.setId(turma.getId());
        turmaExistente.setNomeTurma(turma.getNomeTurma());
        turmaExistente.setInstrumento(turma.getInstrumento());
        turmaExistente.setPeriodo(turma.getPeriodo());
        turmaExistente.setSerie(turma.getSerie());
        return turmaRepository.save(turmaExistente);
    }


    // Excluir Produto ( Exclusão Física )

    public void delete(int id) {
        Turma turmaExistente = findById(id);
        turmaRepository.delete(turmaExistente);
    }


}

