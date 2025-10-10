package com.itb.inf2dm.absencemanager.model.services;

import com.itb.inf2dm.absencemanager.model.entity.Aula;
import com.itb.inf2dm.absencemanager.model.repository.AulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// @Autowired: Injeção de Dependência, ou seja, a referida classe exige o objeto declarado abaixo.

@Service
public class AulaService {

    @Autowired
    private AulaRepository aulaRepository;


    // Listar todos os produtos
    public List<Aula> findAll() {
        return aulaRepository.findAll();
    }

    // Salvar Produto
    public Aula save(Aula aula) {
        aula.setConteudo("");
        return aulaRepository.save(aula);
    }

    // Listar Produto por Id
    public Aula findById(int id) {
        return aulaRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Aula não encontrado com o id:" + id));
    }

    // Atualizar Produto
    public Aula update(int id, Aula aula) {
        Aula aulaExistente = findById(id);
        aulaExistente.setConteudo(aula.getConteudo());
        aulaExistente.setId(aula.getId());
        aulaExistente.setTurma_id(aula.getTurma_id());
        aulaExistente.setDataAula(aula.getDataAula());
        aulaExistente.setHorarioAula(aula.getHorarioAula());
        aulaExistente.setUsuario_id(aula.getUsuario_id());
        aulaExistente.setDisciplina_id(aula.getDisciplina_id());
        aulaExistente.setQrCode(aula.getQrCode());
        return aulaRepository.save(aulaExistente);
    }


    // Excluir Aluno
    public void delete(int id) {
        Aula aulaExistente = findById(id);
        aulaRepository.delete(aulaExistente);
    }
}
