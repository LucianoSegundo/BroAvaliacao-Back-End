package com.LFSoftware.BroAvaliacao.Entidade;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "destinatario_id")
    private Usuario destinatario;

    private Timestamp dataCriacao;
    private String mensagem;
    private String local;
    
	public Notificacao() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Usuario getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(Usuario destinatario) {
		this.destinatario = destinatario;
	}
	public Timestamp getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(Timestamp dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
    
    
}

