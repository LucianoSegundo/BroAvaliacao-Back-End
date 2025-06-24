package com.LFSoftware.BroAvaliacao.Entidade;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
	private Item localItem;

	@ManyToOne
	private Restaurante localRestaurante;

	@OneToMany(mappedBy = "logVinculado", cascade = CascadeType.ALL)
	private List<RepresentacaoModificacao> ListaModificacoes;

	private Long categoriaAtualizacao;

	private String justificativaAlualizacao;

	private LocalDateTime dataCriacao;

	// private Long posicao; // atributo suspenso devido posibilidade de remoção

	public LogAtualizacao() {
	}

	public LogAtualizacao(Usuario autor, Restaurante localRestaurante, Long categoria) {
		this.localRestaurante = localRestaurante;

		inicializar(autor, categoria);
	}

	public LogAtualizacao(Usuario autor, Restaurante localRestaurante, Long categoria,
			String justificativaAlualizacao) {
		this.localRestaurante = localRestaurante;
		this.justificativaAlualizacao = justificativaAlualizacao;

		inicializar(autor, categoria);
	}

	public LogAtualizacao(Usuario autor, Item localItem, Long categoria) {
		this.localItem = localItem;

		inicializar(autor, categoria);
	}

	public LogAtualizacao(Usuario autor, Item localItem, Long categoria, String justificativaAlualizacao) {
		this.localItem = localItem;
		this.justificativaAlualizacao = justificativaAlualizacao;

		inicializar(autor, categoria);
	}

	private void inicializar(Usuario autor, Long categoria) {
		this.autor = autor;
		this.dataCriacao = LocalDateTime.now();

		if (categoria > 4 || categoria < 1)
			throw new ErroGeracaoRetorno("Tipo de atualização não reconhecido.");
		this.categoriaAtualizacao = categoria;
	}

	public String gerarMensagem() {

		String mensagem = "";

		if (categoriaAtualizacao > 4 || categoriaAtualizacao < 1)
			throw new ErroGeracaoRetorno("Falha ao montar o Log, tipode atualização não encontrado");

		if (localRestaurante != null) {

			mensagem = "O Restaurante ";
			mensagem += localRestaurante.getNome();

			if (categoriaAtualizacao == 1)
				mensagem += TipoAtualizacao.criacaoR.getTipo();
			else if (categoriaAtualizacao == 2)
				mensagem += TipoAtualizacao.atualizacao.getTipo();
			else if (categoriaAtualizacao == 3)
				mensagem += TipoAtualizacao.delecao.getTipo();
			else if (categoriaAtualizacao == 4)
				mensagem = "Os dados do Restaurante " + localRestaurante.getNome() + " "
						+ TipoAtualizacao.edicao.getTipo();

		} else if (localItem != null) {

			mensagem = "O Item ";
			mensagem += localItem.getNome();

			if (categoriaAtualizacao == 1)
				mensagem += TipoAtualizacao.criacaoR.getTipo();
			else if (categoriaAtualizacao == 2)
				mensagem += TipoAtualizacao.atualizacao.getTipo();
			else if (categoriaAtualizacao == 3)
				mensagem += TipoAtualizacao.delecao.getTipo();
			else if (categoriaAtualizacao == 4)
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

//	@Transactional(readOnly = true)
//	private void definirPosisao( Long idAlvo, LogRepository repo) {
//		// Objetivo desse metodo é criar uma ordenação entre as diferentes categorias de log para um certo alvo, por exemplo definir que este é a quarta vez que o item foi modificado.
//		//  O metodo está implementado, contudo não é utulizado, pois ainda é questionavel se ela abordagem será util ou não. adição feita em 24/ junho 2025 as 16:16 horas.
//	
//		List<LogAtualizacao> historicoTipo;
//		if(localRestaurante != null && localItem == null) {
//			
//			historicoTipo = repo.findAllByLocalRestaurante_IdAndTipoAtualizacao(idAlvo, tipoAtualizacao);
//			long tamanho = historicoTipo.size();
//			
//			if(tamanho == 0) this.posicao = 1l;
//			else this.posicao = tamanho+1;
//		}
//		
//		else if(localItem != null && localRestaurante == null) {
//			historicoTipo = repo.findAllByLocalItemAndTipoAtualizacao(idAlvo, tipoAtualizacao);
//			long tamanho = historicoTipo.size();
//			
//			if(tamanho == 0) this.posicao = 1l;
//			else this.posicao = tamanho+1;
//		}
//	}

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

	public Long getCategoriaAtualizacao() {
		return categoriaAtualizacao;
	}

	public void setCategoriaAtualizacao(Long categoriaAtualizacao) {
		this.categoriaAtualizacao = categoriaAtualizacao;
	}

	public List<RepresentacaoModificacao> getListaModificacoes() {
		return ListaModificacoes;
	}

	public void setListaModificacoes(List<RepresentacaoModificacao> listaModificacoes) {
		ListaModificacoes = listaModificacoes;
	}

	public String getJustificativaAlualizacao() {
		return justificativaAlualizacao;
	}

	public void setJustificativaAlualizacao(String justificativaAlualizacao) {
		this.justificativaAlualizacao = justificativaAlualizacao;
	}

//	public Long getPosicao() {
//		return posicao;
//	}
//
//	public void setPosicao(Long posicao) {
//		this.posicao = posicao;
//	}
}
