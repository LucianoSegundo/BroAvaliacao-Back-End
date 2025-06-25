package com.LFSoftware.BroAvaliacao.Servicos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.LFSoftware.BroAvaliacao.Controladores.DTO.request.RestauranteAlteradoDTO;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.request.RestauranteDTO;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.response.LogResponse;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.response.RestauranteResponse;
import com.LFSoftware.BroAvaliacao.Entidade.LogAtualizacao;
import com.LFSoftware.BroAvaliacao.Entidade.RepresentacaoModificacao;
import com.LFSoftware.BroAvaliacao.Entidade.Restaurante;
import com.LFSoftware.BroAvaliacao.Entidade.Usuario;
import com.LFSoftware.BroAvaliacao.Excecoes.AcessoNegadoException;
import com.LFSoftware.BroAvaliacao.Excecoes.AusendiaDadosException;
import com.LFSoftware.BroAvaliacao.Excecoes.EntidadeNaoEncontradaException;
import com.LFSoftware.BroAvaliacao.Repositorio.LogRepository;
import com.LFSoftware.BroAvaliacao.Repositorio.RestauranteRepository;
import com.LFSoftware.BroAvaliacao.Repositorio.UsuarioRepository;

public class ItemService {

	@Autowired
	private LogRepository logRepo;
	@Autowired
	private UsuarioRepository usuarioRepo;
	@Autowired
	private RestauranteRepository repositorio;

	@Transactional
	public RestauranteResponse criar(long usuarioID, RestauranteDTO dadosRequeridos) {

		// verificando se os atributos são validos
		if (dadosRequeridos.nome() == null || dadosRequeridos.nome().isBlank())
			throw new AusendiaDadosException("Nome não pode ser nulo.");
		if (dadosRequeridos.Abertura() == null)
			throw new AusendiaDadosException("Horario de abertura não pode ser nula.");
		if (dadosRequeridos.fechamento() == null)
			throw new AusendiaDadosException("Horario de fechamento não pode ser nula.");

		// montando as entidades necessárias para a persistencia.
		Usuario usuario = usuarioRepo.findById(usuarioID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario não encontrado."));

		Restaurante restaurante = new Restaurante();
		restaurante.setNome(dadosRequeridos.nome());
		restaurante.setAbertura(dadosRequeridos.Abertura());
		restaurante.setFechamento(dadosRequeridos.fechamento());

		LogAtualizacao log = new LogAtualizacao(usuario, restaurante, 1l);

		usuario.getHistoricoInteracoes().add(log);
		restaurante.getHistoricoInteracoes().add(log);

		return repositorio.save(restaurante).toResponse();
	}

	@Transactional
	public void deletar(Long restauID, long usuarioID, String justificativa) {

		
		if (justificativa == null || justificativa.isBlank())
			throw new AcessoNegadoException("justificativa para a exclusão não pode ser nula.");

		// resgatando as entidades envolvidas;

		Usuario usuario = usuarioRepo.findById(usuarioID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario não encontrado."));
		Restaurante restaurante = repositorio.findById(restauID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Restaurante não encontrado."));

		if (restaurante.getDelecaoLogica() == true)
			throw new AcessoNegadoException("Restaurante já foi deletado.");

		// definindo logicamente a exclusão do item como verdadeira e fazendo a
		// persistencia das alterações.
		restaurante.setDelecaoLogica(true);

		LogAtualizacao log = new LogAtualizacao(usuario, restaurante, 3l, justificativa);

		usuario.getHistoricoInteracoes().add(log);
		restaurante.getHistoricoInteracoes().add(log);

		repositorio.save(restaurante);

	}

	@Transactional
	public RestauranteResponse editar(Long restauID, long usuarioID, RestauranteAlteradoDTO dadosRequeridos ) {

		if (dadosRequeridos.justificativa() == null || dadosRequeridos.justificativa().isBlank())
			throw new AcessoNegadoException("justificativa para a edição não pode ser nula.");

		Boolean editado = false;

		List<RepresentacaoModificacao> modificacoes = new ArrayList<RepresentacaoModificacao>();

		Restaurante restaurante = repositorio.findById(restauID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Restaurante não encontrado."));

		// verificando se houve modificações e preparando as informações necessarias
		// para a persistencia.
		if (dadosRequeridos.nome() != null && dadosRequeridos.nome().isBlank() == false) {
			if (restaurante.getNome().equals(dadosRequeridos.nome()) == false) {

				editado = true;

				modificacoes.add(new RepresentacaoModificacao(restaurante.getNome(), "Nome do Restaurante",
						dadosRequeridos.nome(), null));

				restaurante.setNome(dadosRequeridos.nome());
			}
		}

		if (dadosRequeridos.Abertura() != null) {
			if (restaurante.getAbertura().equals(dadosRequeridos.Abertura()) == false) {

				editado = true;

				modificacoes.add(new RepresentacaoModificacao(restaurante.getAbertura().toString(),
						"horario de abertura", dadosRequeridos.Abertura().toString(), null));

				restaurante.setAbertura(dadosRequeridos.Abertura());
			}
		}
		if (dadosRequeridos.fechamento() != null) {
			if (restaurante.getFechamento().equals(dadosRequeridos.fechamento()) == false) {

				editado = true;

				modificacoes.add(new RepresentacaoModificacao(restaurante.getFechamento().toString(),
						"horario de fechamento", dadosRequeridos.fechamento().toString(), null));

				restaurante.setFechamento(dadosRequeridos.fechamento());
			}
		}

		// fazendo a persistencia somente se houve modificações.
		if (editado == false)
			throw new AcessoNegadoException("Nenhum atributo foi modificado");

		Usuario usuario = usuarioRepo.findById(usuarioID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario não encontrado."));

		LogAtualizacao log = new LogAtualizacao(usuario, restaurante, 4l, dadosRequeridos.justificativa());

		modificacoes.forEach(x -> x.setLogVinculado(log));
		log.setListaModificacoes(modificacoes);
		usuario.getHistoricoInteracoes().add(log);
		restaurante.getHistoricoInteracoes().add(log);

		return repositorio.save(restaurante).toResponse();
	}

	@Transactional(readOnly = true)
	public Page<RestauranteResponse> listar(PageRequest request) {

		return repositorio.findAllByDelecaoLogica(false, request).map(x -> x.toResponse());

	}

	@Transactional(readOnly = true)
	public Page<LogResponse> retornarLogRestauranteS(Long restauranteId, PageRequest request) {

		return logRepo.findAllByLocalRestaurante_Id(restauranteId, request).map(x -> x.toResponse());

	}

}
