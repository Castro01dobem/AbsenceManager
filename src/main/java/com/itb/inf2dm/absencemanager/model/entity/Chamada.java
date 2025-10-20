package com.itb.inf2dm.absencemanager.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Chamada {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        @Column(length =  100, nullable = false)
        private int aulaId;
        @Column(length =  100, nullable = false)
        private int alunoId;
        @Column(length =  8, nullable = false)
        private LocalDateTime dataCadastro;
        @Column(length =  50, nullable = false)
        private String statusChamada;
        @Column(length =  255, nullable = false)
        private String ocorrencia;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //-----------------------

    public int getAulaId() {
        return aulaId;
    }

    public void setAulaId(int aulaId) {
        this.aulaId = aulaId;
    }

    //-----------------------

    public int getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(int alunoId) {
        this.alunoId = alunoId;
    }

    //-----------------------

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    //-----------------------

    public String getStatusChamada() {
        return statusChamada;
    }

    public void setStatusChamada(String statusChamada) {
        this.statusChamada = statusChamada;
    }

    //-----------------------

    public String getOcorrencia() {
        return ocorrencia;
    }

    public void setOcorrencia(String ocorrencia) {
        this.ocorrencia = ocorrencia;
    }

}

