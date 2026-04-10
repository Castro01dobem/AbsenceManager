package com.itb.inf2dm.absencemanager.model.services;

import com.itb.inf2dm.absencemanager.model.entity.Mensagem;
import com.itb.inf2dm.absencemanager.model.repository.MensagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MensagemService {

    @Autowired
    private MensagemRepository mensagemRepository;

    public List<Mensagem> findAll() {
        return mensagemRepository.findAll();
    }

    public Mensagem save(Mensagem mensagem) {
        mensagem.setId(null);
        mensagem.setDataMensagem(LocalDateTime.now());
        mensagem.setStatusMensagem("ATIVO");
        return mensagemRepository.save(mensagem);
    }

    public Mensagem findById(int id) {
        return mensagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensagem não encontrada com o id: " + id));
    }

    public Mensagem update(int id, Mensagem mensagem) {
        Mensagem existente = findById(id);
        existente.setEmissor(mensagem.getEmissor());
        existente.setEmail(mensagem.getEmail());
        existente.setTelefone(mensagem.getTelefone());
        existente.setTexto(mensagem.getTexto());
        existente.setStatusMensagem(mensagem.getStatusMensagem());
        existente.setDataAtualizacao(LocalDateTime.now());
        return mensagemRepository.save(existente);
    }

    public void delete(int id) {
        mensagemRepository.delete(findById(id));
    }
}
