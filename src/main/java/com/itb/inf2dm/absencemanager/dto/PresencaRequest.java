package com.itb.inf2dm.absencemanager.dto;

public class PresencaRequest {

    private Integer alunoRm;
    private Long aulaId;
    private String token;
    
    public Integer getAlunoRm() {
        return alunoRm;
    }
    public void setAlunoRm(Integer alunoRm) {
        this.alunoRm = alunoRm;
    }
    public Long getAulaId() {
        return aulaId;
    }
    public void setAulaId(Long aulaId) {
        this.aulaId = aulaId;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    
}