package com.itb.inf2dm.absencemanager.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AlunoTurma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 100, nullable = false)
    private int aluno_id;
    @Column(length = 100, nullable = false)
    private int turma_id;
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

    public int getAluno_id() {
        return aluno_id;
    }

    public void setAluno_id(int aluno_id) {
        this.aluno_id = aluno_id;
    }

    //-----------------------

    public int getTurma_id() {
        return turma_id;
    }

    public void setTurma_id(int turma_id) {
        this.turma_id = turma_id;
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
