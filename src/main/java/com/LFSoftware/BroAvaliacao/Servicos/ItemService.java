package com.LFSoftware.BroAvaliacao.Servicos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.LFSoftware.BroAvaliacao.Controladores.DTO.request.ItemAlteradoDTO;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.request.ItemDTO;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.response.ItemResponse;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.response.LogResponse;
import com.LFSoftware.BroAvaliacao.Entidade.Item;
import com.LFSoftware.BroAvaliacao.Entidade.LogAtualizacao;
import com.LFSoftware.BroAvaliacao.Entidade.RepresentacaoModificacao;
import com.LFSoftware.BroAvaliacao.Entidade.Restaurante;
import com.LFSoftware.BroAvaliacao.Entidade.Usuario;
import com.LFSoftware.BroAvaliacao.Excecoes.AcessoNegadoException;
import com.LFSoftware.BroAvaliacao.Excecoes.AusendiaDadosException;
import com.LFSoftware.BroAvaliacao.Excecoes.EntidadeNaoEncontradaException;
import com.LFSoftware.BroAvaliacao.Repositorio.ItemRepository;
import com.LFSoftware.BroAvaliacao.Repositorio.LogRepository;
import com.LFSoftware.BroAvaliacao.Repositorio.RestauranteRepository;
import com.LFSoftware.BroAvaliacao.Repositorio.UsuarioRepository;

@Service
public class ItemService {

	@Autowired
	private LogRepository logRepo;
	@Autowired
	private ItemRepository repositorio;
	@Autowired
	private UsuarioRepository usuarioRepo;
	@Autowired
	private RestauranteRepository restaRepo;

	@Transactional
	public ItemResponse criar(Long restauID, ItemDTO dadosRequeridos, long usuarioID) {

		// verificando se os atributos são validos
		if (dadosRequeridos.nome() == null || dadosRequeridos.nome().isBlank())
			throw new AusendiaDadosException("Nome não pode ser nulo.");
		if (dadosRequeridos.descrição() == null || dadosRequeridos.descrição().isBlank())
			throw new AusendiaDadosException("Descrição não pode ser nula.");

		// montando as entidades necessárias para a persistencia.
		Usuario usuario = usuarioRepo.findById(usuarioID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario não encontrado."));
		Restaurante restaurante = restaRepo.findById(restauID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Restaurante não encontrado."));

		Item entidade = new Item();

		entidade.setRestaurante(restaurante);
		entidade.setNome(dadosRequeridos.nome());
		entidade.setDescricao(dadosRequeridos.descrição());

		LogAtualizacao log = new LogAtualizacao(usuario, entidade, 1l);

		restaurante.getCardapio().add(entidade);
		usuario.getHistoricoInteracoes().add(log);
		entidade.getHistoricoInteracoes().add(log);

		return repositorio.save(entidade).toResponse();
	}

	@Transactional
	public void deletar(Long itemID, Long restauID, String justificativa, long usuarioID) {

		if (justificativa == null || justificativa.isBlank())
			throw new AcessoNegadoException("justificativa para a exclusão não pode ser nula.");

		// resgatando as entidades envolvidas;

		Usuario usuario = usuarioRepo.findById(usuarioID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario não encontrado."));
		Item entidade = repositorio.findById(restauID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Item não encontrado."));

		if (entidade.getDelecaoLogica() == true)
			throw new AcessoNegadoException("Item já foi deletado.");

		if (entidade.getRestaurante().getId() != restauID)
			throw new AcessoNegadoException(
					"Item não pertence ao restaurante referenciado na requisição, devido a divergencia para prevenir erros a exclusão foi negada.");

		// definindo logicamente a exclusão do item como verdadeira e fazendo a
		// persistencia das alterações.

		entidade.setDelecaoLogica(true);

		LogAtualizacao log = new LogAtualizacao(usuario, entidade, 3l, justificativa);

		usuario.getHistoricoInteracoes().add(log);
		entidade.getHistoricoInteracoes().add(log);

		repositorio.save(entidade);

	}

	@Transactional
	public ItemResponse editar(Long itemID, Long restauID, ItemAlteradoDTO dadosRequeridos, long usuarioID) {

		if (dadosRequeridos.justificativa() == null || dadosRequeridos.justificativa().isBlank())
			throw new AcessoNegadoException("justificativa para a edição não pode ser nula.");

		Boolean editado = false;

		List<RepresentacaoModificacao> modificacoes = new ArrayList<RepresentacaoModificacao>();

		Item entidade = repositorio.findById(itemID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Restaurante não encontrado."));

		if (entidade.getRestaurante().getId() != restauID)
			throw new AcessoNegadoException(
					"Item não pertence ao restaurante referenciado na requisição, devido a divergencia para prevenir erros a exclusão foi negada.");

		// verificando se houve modificações e preparando as informações necessarias
		// para a persistencia.
		if (dadosRequeridos.nome() != null && dadosRequeridos.nome().isBlank() == false) {
			if (entidade.getNome().equals(dadosRequeridos.nome()) == false) {

				editado = true;

				modificacoes.add(
						new RepresentacaoModificacao(entidade.getNome(), "Nome do Item", dadosRequeridos.nome(), null));

				entidade.setNome(dadosRequeridos.nome());
			}
		}

		if (dadosRequeridos.descrição() != null && dadosRequeridos.descrição().isBlank() == false) {
			if (entidade.getDescricao().equals(dadosRequeridos.descrição()) == false) {

				editado = true;

				modificacoes.add(new RepresentacaoModificacao(entidade.getNome(), "Descrição do Item",
						dadosRequeridos.nome(), null));

				entidade.setNome(dadosRequeridos.nome());
			}
		}

		// fazendo a persistencia somente se houve modificações.
		if (editado == false)
			throw new AcessoNegadoException("Nenhum atributo foi modificado");

		Usuario usuario = usuarioRepo.findById(usuarioID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario não encontrado."));

		LogAtualizacao log = new LogAtualizacao(usuario, entidade, 4l, dadosRequeridos.justificativa());

		log.setListaModificacoes(modificacoes);
		usuario.getHistoricoInteracoes().add(log);
		entidade.getHistoricoInteracoes().add(log);
		modificacoes.forEach(x -> x.setLogVinculado(log));

		return repositorio.save(entidade).toResponse();
	}

	@Transactional(readOnly = true)
	public Page<ItemResponse> listar(Long restauID, PageRequest request) {

		return repositorio.findAllByDelecaoLogicaAndRestaurante_Id(false, restauID, request).map(x -> x.toResponse());

	}

	@Transactional(readOnly = true)
	public Page<LogResponse> retornarLogitem(Long itemID, PageRequest request) {

		return logRepo.findAllByLocalItem_Id(itemID, request).map(x -> x.toResponse());

	}

}
