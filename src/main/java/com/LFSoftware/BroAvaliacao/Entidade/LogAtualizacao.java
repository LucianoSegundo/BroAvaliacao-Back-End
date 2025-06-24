package com.LFSoftware.BroAvaliacao.Entidade;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.LFSoftware.BroAvaliacao.Controladores.DTO.LogResponse;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.ModificacoesResponse;
import com.LFSoftware.BroAvaliacao.Controladores.Exception.ErroGeracaoRetorno;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class LogAtualizacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Usuario autor;

	@ManyToOne
	private Restaurante localRestaurante;

	@ManyToOne
	private Item localItem;

	@OneToMany(mappedBy = "logVinculado", cascade = CascadeType.ALL)
	private List<RepresentacaoModificacao> ListaModificacoes;

	private LocalDateTime dataCriacao;

	private Long tipoAtualizacao;

	public LogAtualizacao() {
	}

	public LogAtualizacao(Usuario autor, Item localItem, Long tipo) {
		this.localItem = localItem;
		this.autor = autor;
		this.dataCriacao = LocalDateTime.now();

		if (tipo > 4 || tipo < 1)
			throw new ErroGeracaoRetorno("Tipo de atualização não reconhecido.");
		this.tipoAtualizacao = tipo;

	}

	public LogAtualizacao(Usuario autor, Restaurante localRestaurante, Long tipo) {
		this.dataCriacao = LocalDateTime.now();
		this.autor = autor;
		this.localRestaurante = localRestaurante;

		if (tipo > 4 || tipo < 1)
			throw new ErroGeracaoRetorno("Tipo de atualização não reconhecido.");
		this.tipoAtualizacao = tipo;

		System.out.println(gerarMensagem());

	}

	public String gerarMensagem() {

		String mensagem = "";

		if (tipoAtualizacao > 4 || tipoAtualizacao < 1)
			throw new ErroGeracaoRetorno("Falha ao montar o Log, tipode atualização não encontrado");

		if (localRestaurante != null) {

			mensagem = "O Restaurante ";
			mensagem += localRestaurante.getNome();

			if (tipoAtualizacao == 1)
				mensagem += TipoAtualizacao.criacaoR.getTipo();
			else if (tipoAtualizacao == 2)
				mensagem += TipoAtualizacao.atualizacao.getTipo();
			else if (tipoAtualizacao == 3)
				mensagem += TipoAtualizacao.delecao.getTipo();
			else if (tipoAtualizacao == 4)
				mensagem = "Os dados do Restaurante "+ localRestaurante.getNome() +" "+ TipoAtualizacao.edicao.getTipo();

		} else if (localItem != null) {

			mensagem = "O Item ";
			mensagem += localItem.getNome();

			if (tipoAtualizacao == 1)
				mensagem += TipoAtualizacao.criacaoR.getTipo();
			else if (tipoAtualizacao == 2)
				mensagem += TipoAtualizacao.atualizacao.getTipo();
			else if (tipoAtualizacao == 3)
				mensagem += TipoAtualizacao.delecao.getTipo();
			else if (tipoAtualizacao == 4)
				mensagem = "Os dados do Item" + TipoAtualizacao.edicao.getTipo();

		} else
			throw new ErroGeracaoRetorno("Falha ao montar o Log de Atualização");

		mensagem += autor.getUsuario() + " ";
		mensagem += TipoAtualizacao.data.getTipo() + " " + formatarData() + " ";
		mensagem += TipoAtualizacao.horario.getTipo() + " " + formatarhora() + ".";

		return mensagem;
	}

	private String formatarData() {

		String rettorno = "";
		DateTimeFormatter formatador;

		formatador = DateTimeFormatter.ofPattern("d MMMM", new Locale("pt", "BR"));
		rettorno = dataCriacao.format(formatador);

		if (dataCriacao.getYear() != LocalDateTime.now().getYear()) {
			rettorno += " de " + dataCriacao.getYear();
		}

		return rettorno;
	}

	private String formatarhora() {

		DateTimeFormatter formatador;

		formatador = DateTimeFormatter.ofPattern("hh:mm", new Locale("pt", "BR"));
		return dataCriacao.format(formatador);

	}

	public LogResponse toResponse() {
		List<ModificacoesResponse> modificacao = ListaModificacoes.stream().map(x -> x.toResponse())
				.collect(Collectors.toList());

		return new LogResponse(gerarMensagem(), modificacao);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getAutor() {
		return autor;
	}

	public void setAutor(Usuario autor) {
		this.autor = autor;
	}

	public Restaurante getLocalRestaurante() {
		return localRestaurante;
	}

	public void setLocalRestaurante(Restaurante localRestaurante) {
		this.localRestaurante = localRestaurante;
	}

	public Item getLocalItem() {
		return localItem;
	}

	public void setLocalItem(Item localItem) {
		this.localItem = localItem;
	}

	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Long getTipoAtualizacao() {
		return tipoAtualizacao;
	}

	public void setTipoAtualizacao(Long tipoAtualizacao) {
		this.tipoAtualizacao = tipoAtualizacao;
	}

	public List<RepresentacaoModificacao> getListaModificacoes() {
		return ListaModificacoes;
	}

	public void setListaModificacoes(List<RepresentacaoModificacao> listaModificacoes) {
		ListaModificacoes = listaModificacoes;
	}
}
