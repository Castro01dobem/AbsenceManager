package com.itb.inf2dm.absencemanager.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Turma")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String instrumento;

    private String periodo;

    private String ano;

    private Integer vagas;

    private String obs;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario professor;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "status_turma")
    private String statusTurma;

    public Turma() {
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(String instrumento) {
        this.instrumento = instrumento;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Integer getVagas() {
        return vagas;
    }

    public void setVagas(Integer vagas) {
        this.vagas = vagas;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Usuario getProfessor() {
        return professor;
    }

    public void setProfessor(Usuario professor) {
        this.professor = professor;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getStatusTurma() {
        return statusTurma;
    }

    public void setStatusTurma(String statusTurma) {
        this.statusTurma = statusTurma;
    }
}
