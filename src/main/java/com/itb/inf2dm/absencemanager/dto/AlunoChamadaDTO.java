package com.itb.inf2dm.absencemanager.dto;

import java.time.LocalDateTime;

public class AlunoChamadaDTO {

    private Integer rm;
    private String nome;
    private String email;
    private String telefone;
    private String statusAluno;
    private String statusPresenca;
    private LocalDateTime dataConfirmacao;

    public AlunoChamadaDTO(Integer rm, String nome, String email, String telefone, String statusAluno,
            String statusPresenca, LocalDateTime dataConfirmacao) {
        this.rm = rm;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.statusAluno = statusAluno;
        this.statusPresenca = statusPresenca;
        this.dataConfirmacao = dataConfirmacao;
    }

    public Integer getRm() {
        return rm;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getStatusAluno() {
        return statusAluno;
    }

    public String getStatusPresenca() {
        return statusPresenca;
    }

    public LocalDateTime getDataConfirmacao() {
        return dataConfirmacao;
    }
}
