package com.itb.inf2dm.absencemanager.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Mensagem")
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime dataMensagem;

    @Column(length = 100, nullable = false)
    private String emissor;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(length = 400, nullable = false)
    private String texto;

    private LocalDateTime dataAtualizacao;

    @Column(length = 10, nullable = false)
    private String statusMensagem; // ATIVO, INATIVO

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDateTime getDataMensagem() { return dataMensagem; }
    public void setDataMensagem(LocalDateTime dataMensagem) { this.dataMensagem = dataMensagem; }
    public String getEmissor() { return emissor; }
    public void setEmissor(String emissor) { this.emissor = emissor; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    public String getStatusMensagem() { return statusMensagem; }
    public void setStatusMensagem(String statusMensagem) { this.statusMensagem = statusMensagem; }
}
