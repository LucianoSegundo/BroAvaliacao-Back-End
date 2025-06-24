package com.LFSoftware.BroAvaliacao.Entidade;

import com.LFSoftware.BroAvaliacao.Controladores.DTO.ModificacoesResponse;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class RepresentacaoModificacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String valorOriginal;

	private String nomeAtributo;
	private String novoValor;

	@ManyToOne()
	private LogAtualizacao logVinculado;

	RepresentacaoModificacao() {

	}

	public RepresentacaoModificacao(String valorOriginal, String nomeAtributo, String novoValor,
			LogAtualizacao logVinculado) {
		super();
		this.valorOriginal = valorOriginal;
		this.nomeAtributo = nomeAtributo;
		this.novoValor = novoValor;
		this.logVinculado = logVinculado;
	}

	public String gerarMensagem() {
		return "O atributo " + this.nomeAtributo + " " + this.valorOriginal + " Foi modificado para: " + this.novoValor
				+ ".";

	}

	public ModificacoesResponse toResponse() {
		// TODO Auto-generated method stub
		return new ModificacoesResponse(gerarMensagem());
	}

	public String getValorOriginal() {
		return valorOriginal;
	}

	public void setValorOriginal(String valorOriginal) {
		this.valorOriginal = valorOriginal;
	}

	public String getNomeAtributo() {
		return nomeAtributo;
	}

	public void setNomeAtributo(String nomeAtributo) {
		this.nomeAtributo = nomeAtributo;
	}

	public String getNovoValor() {
		return novoValor;
	}

	public void setNovoValor(String novoValor) {
		this.novoValor = novoValor;
	}

	public LogAtualizacao getLogVinculado() {
		return logVinculado;
	}

	public void setLogVinculado(LogAtualizacao logVinculado) {
		this.logVinculado = logVinculado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
