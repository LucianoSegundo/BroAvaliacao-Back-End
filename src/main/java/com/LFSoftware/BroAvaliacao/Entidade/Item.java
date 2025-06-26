package com.LFSoftware.BroAvaliacao.Entidade;

import java.util.ArrayList;
import java.util.List;

import com.LFSoftware.BroAvaliacao.Controladores.DTO.response.ItemResponse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;
	private String descricao;
	private Boolean delecaoLogica;

	@ManyToOne
	@JoinColumn(name = "restaurante_id")
	private Restaurante restaurante;

	@OneToMany(mappedBy = "localItem", cascade = CascadeType.PERSIST)
	private List<LogAtualizacao> historicoInteracoes;

	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
	private List<Resenha> resenhas;

	public Item() {
		super();
		this.historicoInteracoes = new ArrayList<LogAtualizacao>();
		this.delecaoLogica = false;
	}

	public ItemResponse toResponse() {

		return new ItemResponse(this.id, this.nome, this.descricao);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Resenha> getResenhas() {
		return resenhas;
	}

	public void setResenhas(List<Resenha> resenhas) {
		this.resenhas = resenhas;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}

	public List<LogAtualizacao> getHistoricoInteracoes() {
		return historicoInteracoes;
	}

	public void setHistoricoInteracoes(List<LogAtualizacao> historicoInteracoes) {
		this.historicoInteracoes = historicoInteracoes;
	}

	public Boolean getDelecaoLogica() {
		return delecaoLogica;
	}

	public void setDelecaoLogica(Boolean delecaoLogica) {
		this.delecaoLogica = delecaoLogica;
	}

}
