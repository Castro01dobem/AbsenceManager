package com.itb.inf2dm.absencemanager.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Usuario {

    //VERIFICAR
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 100, nullable = true)
    private String nome;
    @Column(length = 45, nullable = false)
    private String email;
    @Column(length = 100, nullable = false)
    private String senha;
    @Column(length = 25, nullable = false)
    private String nivelAcesso;
    @Column(nullable = true)
    private byte[] foto;
    @Column(length =8, nullable = false)
    private LocalDateTime dataCadastro;
    @Column(length = 25, nullable = false)
    private String statusUsuario;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //-----------------------

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    //-----------------------

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //-----------------------

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    //-----------------------

    public String getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(String nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }

    //-----------------------

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    //-----------------------

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    //-----------------------

    public String getStatusUsuario() {
        return statusUsuario;
    }

    public void setStatusUsuario(String statusUsuario) {
        this.statusUsuario = statusUsuario;
    }

}
