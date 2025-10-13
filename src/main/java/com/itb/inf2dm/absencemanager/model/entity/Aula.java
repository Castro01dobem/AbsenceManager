package com.itb.inf2dm.absencemanager.model.entity;

import jakarta.persistence.*;

import java.sql.Time;
import java.time.LocalDateTime;

@Entity
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length =4, nullable = false)
    private Time horarioAula;
    @Column(length =8, nullable = false)
    private LocalDateTime dataAula;
    @Column(length = 100, nullable = false)
    private int usuario_id;
    @Column(length = 100, nullable = false)
    private int disciplina_id;
    @Column(length = 100, nullable = false)
    private int turma_id;
    @Column(length = 100, nullable = false)
    private String conteudo;
    private byte[] qrCode;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //-----------------------

    public Time getHorarioAula() {
        return horarioAula;
    }

    public void setHorarioAula(Time horarioAula) {
        this.horarioAula = horarioAula;
    }

    //-----------------------

    public LocalDateTime getDataAula() {
        return dataAula;
    }

    public void setDataAula(LocalDateTime dataAula) {
        this.dataAula = dataAula;
    }

    //-----------------------

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    //-----------------------

    public int getDisciplina_id() {
        return disciplina_id;
    }

    public void setDisciplina_id(int disciplina_id) {
        this.disciplina_id = disciplina_id;
    }

    //-----------------------

    public int getTurma_id() {
        return turma_id;
    }

    public void setTurma_id(int turma_id) {
        this.turma_id = turma_id;
    }

    //-----------------------

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    //-----------------------

    public byte[] getQrCode() {
        return qrCode;
    }

    public void setQrCode(byte[] qrCode) {
        this.qrCode = qrCode;
    }

}

