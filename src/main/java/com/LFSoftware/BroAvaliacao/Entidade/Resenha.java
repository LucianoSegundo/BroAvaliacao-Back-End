package com.LFSoftware.BroAvaliacao.Entidade;

import java.time.LocalDateTime;
import java.util.List;

import com.LFSoftware.BroAvaliacao.Controladores.DTO.response.ResenhaResponse;
import com.LFSoftware.BroAvaliacao.Excecoes.AcessoNegadoException;
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
public class Resenha {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String titulo;
	private String conteudo;
	private LocalDateTime dataCriacao;

	@ManyToOne
	@JoinColumn(name = "autor_id")
	private Usuario autor;

	@ManyToOne
	@JoinColumn(name = "restaurante_id")
	private Restaurante restaurante;

	@ManyToOne
	@JoinColumn(name = "item_id")
	private Item item;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "resenha")
	private List<Comentario> comentarios;

	public Resenha() {
		super();
		this.dataCriacao = LocalDateTime.now();
	}

	public ResenhaResponse toResponse() {
		String alvo = "";
		Boolean isRestaurante = false;
		String dataa = FormTempo.formatarData(dataCriacao);

		if (restaurante != null) {
			isRestaurante = true;
			alvo = this.restaurante.getNome();
		} else if (item != null) {
			isRestaurante = false;
			alvo = this.item.getNome();
		} else
			throw new AcessoNegadoException("Algo deu errado, resenha apontando para nada.");

		return new ResenhaResponse(id, titulo, conteudo, autor.getUsuario(), alvo, dataa, isRestaurante);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Usuario getAutor() {
		return autor;
	}

	public void setAutor(Usuario autor) {
		this.autor = autor;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public List<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

	public LocalDateTime getCriacao() {
		return dataCriacao;
	}

	public void setCriacao(LocalDateTime criacao) {
		this.dataCriacao = criacao;
	}

}
