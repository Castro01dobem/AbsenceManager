package com.itb.inf2dm.absencemanager.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Aula")
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false)
    private String tituloAula;

    @Column(length = 200)
    private String instrumento;

    @Column(nullable = false)
    private LocalDate dataAula;

    @Column(length = 20, nullable = false)
    private String hora;

    @Column(length = 20)
    private String duracao;

    @Column(length = 200)
    private String obs;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime dataCadastro;

    private LocalDateTime dataAtualizacao;

    @Column(length = 20, nullable = false)
    private String statusAula; // ATIVO, INATIVO

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTituloAula() { return tituloAula; }
    public void setTituloAula(String tituloAula) { this.tituloAula = tituloAula; }
    public String getInstrumento() { return instrumento; }
    public void setInstrumento(String instrumento) { this.instrumento = instrumento; }
    public LocalDate getDataAula() { return dataAula; }
    public void setDataAula(LocalDate dataAula) { this.dataAula = dataAula; }
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
    public String getDuracao() { return duracao; }
    public void setDuracao(String duracao) { this.duracao = duracao; }
    public String getObs() { return obs; }
    public void setObs(String obs) { this.obs = obs; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    public String getStatusAula() { return statusAula; }
    public void setStatusAula(String statusAula) { this.statusAula = statusAula; }
}
