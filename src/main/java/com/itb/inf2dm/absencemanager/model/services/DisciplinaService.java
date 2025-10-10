package com.itb.inf2dm.absencemanager.model.services;

import com.itb.inf2dm.absencemanager.model.entity.Disciplina;
import com.itb.inf2dm.absencemanager.model.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// @Autowired : Injeção de Dependência, ou seja, a referida classe exige o objeto declarado abaixo.

@Service
public class DisciplinaService {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    // Listar todos as disciplinas

    public List<Disciplina> findAll() {
        return disciplinaRepository.findAll();
    }

    // Salvar Disciplina

    public Disciplina save(Disciplina disciplina) {
        disciplina.setNomeDisciplina("");
        return disciplinaRepository.save(disciplina);
    }

    // Listar Disciplina por Id

    public Disciplina findById(int id) {
        return disciplinaRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Disciplina não encontrada com o id: " + id));
    }


    // Atualizar Disciplina

    public Disciplina update(int id, Disciplina disciplina) {

        Disciplina disciplinaExistente = findById(id);
        disciplinaExistente.setId(disciplina.getId());
        disciplinaExistente.setNomeDisciplina(disciplina.getNomeDisciplina());
        return disciplinaRepository.save(disciplinaExistente);
    }


    // Excluir Disciplina ( Exclusão Física )

    public void delete(int id) {
        Disciplina disciplinaExistente = findById(id);
        disciplinaRepository.delete(disciplinaExistente);
    }


}
