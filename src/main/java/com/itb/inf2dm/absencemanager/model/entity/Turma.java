package com.itb.inf2dm.absencemanager.model.entity;

import jakarta.persistence.*;

@Entity
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 50, nullable = true)
    private String nomeTurma;
    @Column(length = 50, nullable = false)
    private String instrumento;
    @Column(length = 20, nullable = true)
    private String serie;
    @Column(length = 20, nullable = false)
    private String periodo;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //-----------------------

    public String getNomeTurma() {
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }

    //-----------------------

    public String getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(String instrumento) {
        this.instrumento = instrumento;
    }

    //-----------------------

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    //-----------------------

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

}

