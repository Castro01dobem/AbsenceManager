package com.itb.inf2dm.absencemanager.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Chamada {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        @Column(length =  100, nullable = false)
        private int aula_id;
        @Column(length =  100, nullable = false)
        private int aluno_id;
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

    public int getAula_id() {
        return aula_id;
    }

    public void setAula_id(int aula_id) {
        this.aula_id = aula_id;
    }

    //-----------------------

    public int getAluno_id() {
        return aluno_id;
    }

    public void setAluno_id(int aluno_id) {
        this.aluno_id = aluno_id;
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

