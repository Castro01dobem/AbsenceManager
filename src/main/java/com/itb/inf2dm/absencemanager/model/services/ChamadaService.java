package com.itb.inf2dm.absencemanager.model.services;

import com.itb.inf2dm.absencemanager.model.entity.Chamada;
import com.itb.inf2dm.absencemanager.model.repository.ChamadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// @Autowired : Injeção de Dependência, ou seja, a referida classe exige o objeto declarado abaixo.

@Service
public class ChamadaService {

    @Autowired
    private ChamadaRepository chamadaRepository;

    // Listar todos os produtos

    public List<Chamada> findAll() {
        return chamadaRepository.findAll();
    }

    // Salvar Chamada

    public Chamada save(Chamada chamada) {
        chamada.setStatusChamada("Não registrado");
        return chamadaRepository.save(chamada);
    }

    // Listar Chamada por Id

    public Chamada findById(int id) {
        return chamadaRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Chamada não encontrado com o id: " + id));
    }


    // Atualizar Chamada

    public Chamada update(int id, Chamada chamada) {

        Chamada chamadaExistente = findById(id);
        chamadaExistente.setStatusChamada(chamada.getStatusChamada());
        chamadaExistente.setId(chamada.getId());
        chamadaExistente.setAlunoId(chamada.getAlunoId());
        chamadaExistente.setAulaId(chamada.getAulaId());
        chamadaExistente.setDataCadastro(chamada.getDataCadastro());
        chamadaExistente.setOcorrencia(chamada.getOcorrencia());
        return chamadaRepository.save(chamadaExistente);
    }


    // Excluir Chamada ( Exclusão Física )

    public void delete(int id) {
        Chamada chamadaExistente = findById(id);
        chamadaRepository.delete(chamadaExistente);
    }


}
