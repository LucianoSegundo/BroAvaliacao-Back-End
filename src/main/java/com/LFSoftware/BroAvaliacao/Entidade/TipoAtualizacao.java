package com.LFSoftware.BroAvaliacao.Entidade;

public enum TipoAtualizacao {

	criacaoR(" Foi criado pelo usuário "),
	atualizacao(" Foi atualizado pelo usuário "),
	delecao(" Foi deletado  pelo usuário "),
	edicao(" foram editados pelo usuário "),
	horario(" as "),
	data(" do dia ");
	

	private final String tipo;
	TipoAtualizacao(String tipo) {
		this.tipo = tipo;
	}
	public String getTipo() {
		return tipo;
	}
	
}
