package com.LFSoftware.BroAvaliacao.Entidade;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.LFSoftware.BroAvaliacao.Controladores.DTO.RestauranteResponse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    
    private LocalTime Abertura;
    
    private LocalTime fechamento;
    
    private Boolean delecaoLogica;

    @OneToOne(mappedBy = "restaurante", cascade = CascadeType.ALL)
    private Endereco endereco;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL)
    private List<Item> cardapio;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL)
    private List<Resenha> resenhas;

    @OneToMany(mappedBy = "localRestaurante", cascade = CascadeType.PERSIST)
	private List<LogAtualizacao> historicoInteracoes; 
    
	public Restaurante() {
		super();
		this.historicoInteracoes = new ArrayList<LogAtualizacao>();
		this.setDelecaoLogica(false);
	}
	
	public RestauranteResponse toResponse() {
		return new RestauranteResponse(id, nome, Abertura.toString(), fechamento.toString());
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

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public List<Item> getCardapio() {
		return cardapio;
	}

	public void setCardapio(List<Item> cardapio) {
		this.cardapio = cardapio;
	}

	public List<Resenha> getResenhas() {
		return resenhas;
	}

	public void setResenhas(List<Resenha> resenhas) {
		this.resenhas = resenhas;
	}

	public LocalTime getAbertura() {
		return Abertura;
	}

	public void setAbertura(LocalTime abertura) {
		Abertura = abertura;
	}

	public LocalTime getFechamento() {
		return fechamento;
	}

	public void setFechamento(LocalTime fechamento) {
		this.fechamento = fechamento;
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

