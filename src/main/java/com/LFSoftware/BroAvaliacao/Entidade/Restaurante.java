package com.LFSoftware.BroAvaliacao.Entidade;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    
    private String Abertura;
    
    private String fechamento;
    
    private Boolean temProprietario;

    @ManyToOne
    @JoinColumn(name = "proprietario_id")
    private Usuario proprietario;

    @OneToOne(mappedBy = "restaurante", cascade = CascadeType.ALL)
    private Endereco endereco;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL)
    private List<Item> cardapio;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL)
    private List<Resenha> resenhas;

	public Restaurante() {
		super();
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

	public Usuario getProprietario() {
		return proprietario;
	}

	public void setProprietario(Usuario proprietario) {
		this.proprietario = proprietario;
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

	public String getAbertura() {
		return Abertura;
	}

	public void setAbertura(String abertura) {
		Abertura = abertura;
	}

	public String getFechamento() {
		return fechamento;
	}

	public void setFechamento(String fechamento) {
		this.fechamento = fechamento;
	}

	public Boolean getTemProprietario() {
		return temProprietario;
	}

	public void setTemProprietario(Boolean temProprietario) {
		this.temProprietario = temProprietario;
	}
    
    
}

