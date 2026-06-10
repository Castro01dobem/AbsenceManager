package com.itb.inf2dm.absencemanager.services;

import com.itb.inf2dm.absencemanager.model.entity.Aula;
import com.itb.inf2dm.absencemanager.model.repository.AulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        // Ajustado para a entidade `Aula` atual (campos: data/status)
        if (aula.getData() == null) {
            aula.setData(java.time.LocalDate.now());
        }
        if (aula.getStatus() == null) {
            aula.setStatus(true);
        }
        return aulaRepository.save(aula);
    }


    public Aula findById(long id) {
        return aulaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aula não encontrada com o id: " + id));
    }

    public Aula update(long id, Aula aula) {
        Aula existente = findById(id);


        existente.setTurma(aula.getTurma());
        existente.setData(aula.getData());
        existente.setHoraInicio(aula.getHoraInicio());
        existente.setHoraFim(aula.getHoraFim());
        existente.setConteudo(aula.getConteudo());
        existente.setStatus(aula.getStatus());
        return aulaRepository.save(existente);
    }

    public void delete(long id) {
        aulaRepository.delete(findById(id));
    }

}
