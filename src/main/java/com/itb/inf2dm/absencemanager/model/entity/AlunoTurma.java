package com.itb.inf2dm.absencemanager.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AlunoTurma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 100, nullable = false)
    private int alunoId;
    @Column(length = 100, nullable = false)
    private int turmaId;
    @Column(length =8, nullable = false)
    private LocalDateTime dataCadastro;
    @Column(length = 25, nullable = false)
    private String statusAlunoTurma;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //-----------------------

    public int getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(int alunoId) {
        this.alunoId = alunoId;
    }

    //-----------------------

    public int getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(int turmaId) {
        this.turmaId = turmaId;
    }

    //-----------------------

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    //-----------------------

    public String getStatusAlunoTurma() {
        return statusAlunoTurma;
    }

    public void setStatusAlunoTurma(String statusAlunoTurma) {
        this.statusAlunoTurma = statusAlunoTurma;
    }

}
