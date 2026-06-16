package com.itb.inf2dm.absencemanager.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ChamadaDetalhesResponseDTO {

    private Long id;
    private Long turmaId;
    private String turmaNome;
    private String token;
    private String qrCodePayload;
    private LocalDateTime dataGeracao;
    private LocalDateTime dataExpiracao;
    private String status;
    private Long totalPresentes;
    private Long totalFaltas;
    private List<AlunoChamadaDTO> alunos;

    public ChamadaDetalhesResponseDTO(Long id, Long turmaId, String turmaNome, String token, String qrCodePayload,
            LocalDateTime dataGeracao, LocalDateTime dataExpiracao, String status, Long totalPresentes,
            Long totalFaltas, List<AlunoChamadaDTO> alunos) {
        this.id = id;
        this.turmaId = turmaId;
        this.turmaNome = turmaNome;
        this.token = token;
        this.qrCodePayload = qrCodePayload;
        this.dataGeracao = dataGeracao;
        this.dataExpiracao = dataExpiracao;
        this.status = status;
        this.totalPresentes = totalPresentes;
        this.totalFaltas = totalFaltas;
        this.alunos = alunos;
    }

    public Long getId() {
        return id;
    }

    public Long getTurmaId() {
        return turmaId;
    }

    public String getTurmaNome() {
        return turmaNome;
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

    public Long getTotalPresentes() {
        return totalPresentes;
    }

    public Long getTotalFaltas() {
        return totalFaltas;
    }

    public List<AlunoChamadaDTO> getAlunos() {
        return alunos;
    }
}
