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
    private int usuarioId;
    @Column(length = 100, nullable = false)
    private int disciplinaId;
    @Column(length = 100, nullable = false)
    private int turmaId;
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

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    //-----------------------

    public int getDisciplinaId() {
        return disciplinaId;
    }

    public void setDisciplinaId(int disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    //-----------------------

    public int getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(int turmaId) {
        this.turmaId = turmaId;
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

