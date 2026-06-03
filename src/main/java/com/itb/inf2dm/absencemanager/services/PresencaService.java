package com.itb.inf2dm.absencemanager.services;

import com.itb.inf2dm.absencemanager.model.entity.Aluno;
import com.itb.inf2dm.absencemanager.model.entity.Aula;
import com.itb.inf2dm.absencemanager.model.entity.Presenca;
import com.itb.inf2dm.absencemanager.model.entity.QRCode;
import com.itb.inf2dm.absencemanager.model.repository.AlunoRepository;
import com.itb.inf2dm.absencemanager.model.repository.AulaRepository;
import com.itb.inf2dm.absencemanager.model.repository.PresencaRepository;
import com.itb.inf2dm.absencemanager.model.repository.QRCodeRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PresencaService {

    @Autowired
    private QRCodeRepository qrCodeRepository;

    @Autowired
    private PresencaRepository presencaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private AulaRepository aulaRepository;

    @Transactional
    public void registrarPresenca(Integer alunoRm, Long aulaId, String token) {

        // 🔍 1. Buscar QR Code
        QRCode qr = qrCodeRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("QR Code inválido"));
        
        Aluno aluno = alunoRepository.findById(alunoRm)
                .orElseThrow(() -> new RuntimeException("Aluno inválido"));

        Aula aula = aulaRepository.findById(aulaId)
                .orElseThrow(() -> new RuntimeException("Aula inválido"));

        // 🔒 2. Validar aula
        if (!qr.getAulaId().equals(aulaId)) {
            throw new RuntimeException("QR não pertence a essa aula");
        }

        // ⛔ 3. Verificar se está ativo
        if (!qr.getAtivo()) {
            throw new RuntimeException("QR Code inativo");
        }

        // ⏱️ 4. Verificar expiração
        if (qr.getDataExpiracao().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("QR Code expirado");
        }

        // 🚫 5. Evitar presença duplicada
        boolean jaRegistrado = presencaRepository
                .existsByAlunoRmAndAulaId(alunoRm, aulaId);

        if (jaRegistrado) {
            throw new RuntimeException("Presença já registrada");
        }

        // ✅ 6. Salvar presença
        Presenca presenca = new Presenca();
        presenca.setAluno(aluno);
        presenca.setAula(aula);
        presenca.setDataCadastro(LocalDateTime.now());
        presenca.setStatusPresenca(true);

        presencaRepository.save(presenca);
    }

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
