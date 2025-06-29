package com.LFSoftware.BroAvaliacao.Entidade;

import com.LFSoftware.BroAvaliacao.Controladores.Exception.ErroGeracaoRetorno;
import com.LFSoftware.BroAvaliacao.Servicos.FormTempo;

public enum TipoAtualizacao {

	criacaoR(1, " Foi Criado pelo Usuário "), 
	delecao(2, " Foi Deletado  pelo Usuário "), 
	edicao(3,  " Foi Atualizado pelo Usuário "),
	adicionarItem(4, " Foi Adicionado no Restaurante "),
	removerItem(5, " Foi Removido do Restaurante ");


	private final int categoria;
	private final String descricao;

	TipoAtualizacao(int categoria, String descricao) {
		this.categoria = categoria;
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public int getCategoria() {
		return categoria;
	}


	public String montarMensagem(LogAtualizacao goL) {

		String nome = "";
		String prefacil = "";
		String autor = goL.getAutor().getUsuario();

		if (goL.getLocalRestaurante() != null && goL.getCategoriaAtualizacao() <=3) {
			prefacil = "O Restaurante";
			nome = goL.getLocalRestaurante().getNome();
			
			
		} else if (goL.getLocalItem() != null) {
			nome = goL.getLocalItem().getNome();
			prefacil = "O Item";
			
		} else if (goL.getLocalRestaurante() != null && goL.getCategoriaAtualizacao() >3) {
			prefacil = "O Item ";
			nome = goL.getNomeItem();
			autor = "pelo usuário " + autor;
			
		} else
			throw new ErroGeracaoRetorno("Falha ao montar o Log de Atualização");

		String mensagem = prefacil + " " + nome + descricao + autor + " as " + FormTempo.formatarhora(goL.getDataCriacao())
				+ " do dia " + FormTempo.formatarData(goL.getDataCriacao()) + ".";

		return mensagem;
	}

	public static TipoAtualizacao getTipo(int categoria) {

		for (TipoAtualizacao enumeracao : TipoAtualizacao.values()) {
			if (enumeracao.getCategoria() == categoria)
				return enumeracao;
		}
		return null;
	};

}
