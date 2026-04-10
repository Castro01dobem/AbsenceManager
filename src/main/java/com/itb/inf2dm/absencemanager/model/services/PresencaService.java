package com.itb.inf2dm.absencemanager.model.services;

import com.itb.inf2dm.absencemanager.model.entity.Presenca;
import com.itb.inf2dm.absencemanager.model.repository.PresencaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PresencaService {

    @Autowired
    private PresencaRepository presencaRepository;

    public List<Presenca> findAll() {
        return presencaRepository.findAll();
    }

    public Presenca save(Presenca presenca) {
        presenca.setId(null);
        presenca.setDataCadastro(LocalDateTime.now());
        return presencaRepository.save(presenca);
    }

    public Presenca findById(int id) {
        return presencaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presença não encontrada com o id: " + id));
    }

    public Presenca update(int id, Presenca presenca) {
        Presenca existente = findById(id);
        existente.setAluno(presenca.getAluno());
        existente.setAula(presenca.getAula());
        existente.setStatusPresenca(presenca.getStatusPresenca());
        return presencaRepository.save(existente);
    }

    public void delete(int id) {
        presencaRepository.delete(findById(id));
    }
}
