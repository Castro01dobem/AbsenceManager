package com.itb.inf2dm.absencemanager.model.services;

import com.itb.inf2dm.absencemanager.model.entity.Aula;
import com.itb.inf2dm.absencemanager.model.repository.AulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AulaService {

    @Autowired
    private AulaRepository aulaRepository;

    public List<Aula> findAll() {
        return aulaRepository.findAll();
    }

    public Aula save(Aula aula) {
        aula.setId(null);
        aula.setDataCadastro(LocalDateTime.now());
        aula.setStatusAula("ATIVO");
        return aulaRepository.save(aula);
    }

    public Aula findById(int id) {
        return aulaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aula não encontrada com o id: " + id));
    }

    public Aula update(int id, Aula aula) {
        Aula existente = findById(id);
        existente.setTituloAula(aula.getTituloAula());
        existente.setInstrumento(aula.getInstrumento());
        existente.setDataAula(aula.getDataAula());
        existente.setHora(aula.getHora());
        existente.setDuracao(aula.getDuracao());
        existente.setObs(aula.getObs());
        existente.setUsuario(aula.getUsuario());
        existente.setStatusAula(aula.getStatusAula());
        existente.setDataAtualizacao(LocalDateTime.now());
        return aulaRepository.save(existente);
    }

    public void delete(int id) {
        aulaRepository.delete(findById(id));
    }
}
