package com.itb.inf2dm.absencemanager.model.entity;


import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Aluno {

    //VERIFICAR
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 100, nullable = true)
    private String nome;
    @Column(length = 1, nullable = true)
    private String sexo;
    @Column(length = 20, nullable = false)
    private String rm;
    @Column(length = 100, nullable = true)
    private String telefone;
    @Column(length = 8, nullable = false)
    private Date dataNascimento;
    @Column(length = 100, nullable = false)
    private int usuario_id;
    @Column(length = 25, nullable = false)
    private String statusAluno;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //-----------------------

    public String nome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    //-----------------------

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    //-----------------------

    public String getRm() {
        return rm;
    }

    public void setRm(String rm) {
        this.rm = rm;
    }

    //-----------------------

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    //-----------------------

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    //-----------------------

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }

    //-----------------------

    public String getStatusAluno() {
        return statusAluno;
    }

    public void setStatusAluno(String statusAluno) {
        this.statusAluno = statusAluno;
    }

}
