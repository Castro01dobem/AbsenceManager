package com.itb.inf2dm.absencemanager.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CriarChamadaResponseDTO {

    private Long id;
    private Long turmaId;
    private String token;
    private String qrCodePayload;
    private LocalDateTime dataGeracao;
    private LocalDateTime dataExpiracao;
    private String status;
    private List<AlunoChamadaDTO> alunos;

    public CriarChamadaResponseDTO(Long id, Long turmaId, String token, String qrCodePayload,
            LocalDateTime dataGeracao, LocalDateTime dataExpiracao, String status, List<AlunoChamadaDTO> alunos) {
        this.id = id;
        this.turmaId = turmaId;
        this.token = token;
        this.qrCodePayload = qrCodePayload;
        this.dataGeracao = dataGeracao;
        this.dataExpiracao = dataExpiracao;
        this.status = status;
        this.alunos = alunos;
    }

    public Long getId() {
        return id;
    }

    public Long getTurmaId() {
        return turmaId;
    }

    public String getToken() {
        return token;
    }

    public String getQrCodePayload() {
        return qrCodePayload;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    public String getStatus() {
        return status;
    }

    public List<AlunoChamadaDTO> getAlunos() {
        return alunos;
    }
}
