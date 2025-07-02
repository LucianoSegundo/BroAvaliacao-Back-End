package com.LFSoftware.BroAvaliacao.Entidade;

import java.time.LocalDateTime;
import java.util.List;

import com.LFSoftware.BroAvaliacao.Controladores.DTO.response.ComentarioResponse;
import com.LFSoftware.BroAvaliacao.Servicos.FormTempo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensagem;
    private long numeroComentarios;
    private LocalDateTime criacao;
 
    @ManyToOne
	@JoinColumn(name = "autor_id")
	private Usuario autor;

    @ManyToOne
	@JoinColumn(name = "resenha_id")
	private Resenha resenha;
    
    @ManyToOne
 	@JoinColumn(name = "comentariopai_id")
 	private Comentario comentariopai;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comentariopai")
    private List<Comentario> comentarios;

	public Comentario() {
		super();
		this.criacao = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public long getNumeroComentarios() {
		return numeroComentarios;
	}

	public void setNumeroComentarios(long numeroComentarios) {
		this.numeroComentarios = numeroComentarios;
	}

	public List<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

	public LocalDateTime getCriacao() {
		return criacao;
	}

	public void setCriacao(LocalDateTime criacao) {
		this.criacao = criacao;
	}

	public Usuario getAutor() {
		return autor;
	}

	public void setAutor(Usuario autor) {
		this.autor = autor;
	}

	public Resenha getResenha() {
		return resenha;
	}

	public void setResenha(Resenha resenha) {
		this.resenha = resenha;
	}

	public ComentarioResponse toResponse() {
		// TODO Auto-generated method stub
		return new ComentarioResponse(this.id, mensagem, this.autor.getUsuario(), FormTempo.formatarData(criacao));
	}

	public Comentario getComentariopai() {
		return comentariopai;
	}

	public void setComentariopai(Comentario comentarioPai) {
		this.comentariopai = comentarioPai;
	}
    
    
}

