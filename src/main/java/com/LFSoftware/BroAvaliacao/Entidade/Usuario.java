package com.LFSoftware.BroAvaliacao.Entidade;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usuario;
    private String senha;
    private String email;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
    private List<Resenha> resenhas;

    @OneToOne(cascade = CascadeType.ALL)
    
    @JoinColumn(name = "configuracoes_id")
    private Configuracao configuracoes;

    @OneToMany(mappedBy = "destinatario", cascade = CascadeType.ALL)
    private List<Notificacao> notificacoes;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Comentario> comentarios;
    
    public Usuario() {
    	super();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Resenha> getResenhas() {
		return resenhas;
	}

	public void setResenhas(List<Resenha> resenhas) {
		this.resenhas = resenhas;
	}

	public Configuracao getConfiguracoes() {
		return configuracoes;
	}

	public void setConfiguracoes(Configuracao configuracoes) {
		this.configuracoes = configuracoes;
	}

	public List<Notificacao> getNotificacoes() {
		return notificacoes;
	}

	public void setNotificacoes(List<Notificacao> notificacoes) {
		this.notificacoes = notificacoes;
	}

	public List<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}
    
    
}

