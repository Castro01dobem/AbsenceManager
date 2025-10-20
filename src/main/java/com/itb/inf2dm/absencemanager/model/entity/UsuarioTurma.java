package com.itb.inf2dm.absencemanager.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class UsuarioTurma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(length =8, nullable = false)
    private LocalDateTime dataCadastro;
    @Column(length = 25, nullable = false)
    private String statusUsuarioTurma;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //-----------------------



    //-----------------------



    //-----------------------

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    //-----------------------

    public String getStatusUsuarioTurma() {
        return statusUsuarioTurma;
    }

    public void setStatusUsuarioTurma(String statusUsuarioTurma) {
        this.statusUsuarioTurma = statusUsuarioTurma;
    }

}
