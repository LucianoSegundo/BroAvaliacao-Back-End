package com.LFSoftware.BroAvaliacao.Servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.LFSoftware.BroAvaliacao.Controladores.DTO.request.ReferenciaResenhaDTO;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.request.ResenhaDTO;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.response.ResenhaResponse;
import com.LFSoftware.BroAvaliacao.Entidade.Item;
import com.LFSoftware.BroAvaliacao.Entidade.Resenha;
import com.LFSoftware.BroAvaliacao.Entidade.Restaurante;
import com.LFSoftware.BroAvaliacao.Entidade.Usuario;
import com.LFSoftware.BroAvaliacao.Excecoes.AcessoNegadoException;
import com.LFSoftware.BroAvaliacao.Excecoes.AusendiaDadosException;
import com.LFSoftware.BroAvaliacao.Excecoes.EntidadeNaoEncontradaException;
import com.LFSoftware.BroAvaliacao.Repositorio.ItemRepository;
import com.LFSoftware.BroAvaliacao.Repositorio.ResenhaRepository;
import com.LFSoftware.BroAvaliacao.Repositorio.RestauranteRepository;
import com.LFSoftware.BroAvaliacao.Repositorio.UsuarioRepository;

@Service
public class ResenhaService {

	@Autowired
	private ResenhaRepository repositorio;
	@Autowired
	private UsuarioRepository usuarioRepo;
	@Autowired
	private RestauranteRepository restRepo;
	@Autowired
	private ItemRepository itemRepo;

	public ResenhaService() {

	}

	@Transactional()
	public ResenhaResponse resenharRestaurante(Long restauID, ResenhaDTO dadosRequeridos, long userID) {

		Resenha entidade = criarResenha(userID, dadosRequeridos);

		Restaurante restaurante = restRepo.findById(restauID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Restaurante não encontrado"));

		entidade.setRestaurante(restaurante);
		restaurante.getResenhas().add(entidade);

		return persistirResenha(entidade);
	}
	
	@Transactional()
	public ResenhaResponse resenharItem(Long itemID, ResenhaDTO dadosRequeridos, long userID) {

		Resenha entidade = criarResenha(userID, dadosRequeridos);

		Item item = itemRepo.findById(itemID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Item não encontrado"));

		entidade.setItem(item);
		item.getResenhas().add(entidade);

		return persistirResenha(entidade);
	}

	@Transactional(readOnly = true)
	public Resenha criarResenha(Long userID, ResenhaDTO dadosRequeridos) {
		if (dadosRequeridos.conteudo() == null || dadosRequeridos.conteudo().isBlank())
			throw new AusendiaDadosException("O conteudo da postagem não pode ser nulo.");
		if (dadosRequeridos.titulo() == null || dadosRequeridos.titulo().isBlank())
			throw new AusendiaDadosException("O titulo da postagem não pode ser nulo.");

		Usuario usuario = usuarioRepo.findById(userID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario não encontrado"));

		Resenha entidade = new Resenha();

		entidade.setTitulo(dadosRequeridos.titulo());
		entidade.setConteudo(dadosRequeridos.conteudo());
		entidade.setAutor(usuario);
		usuario.getResenhas().add(entidade);

		return entidade;
	}

	@Transactional()
	public ResenhaResponse persistirResenha(Resenha resenha) {

		return repositorio.save(resenha).toResponse();
	}

	@Transactional()
	public void deletar(Long resenhaID, long userID) {
		Resenha resenha = validarAutoria(resenhaID, userID,  "exclui-la");

		repositorio.deleteById(resenha.getId());
	};
	
	@Transactional()
	public ReferenciaResenhaDTO retornarReferencia(Long resenhaID) {

		Resenha resenha = repositorio.findById(resenhaID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Resenha não encontrada."));

		Long autorID = resenha.getAutor().getId();
		Long alvoID;
		Boolean isRestaurante;
		if (resenha.getRestaurante() != null) {
			alvoID = resenha.getRestaurante().getId();
			isRestaurante = true;
		} else {
			alvoID = resenha.getItem().getId();
			isRestaurante = false;
		}

		ReferenciaResenhaDTO referencia = new ReferenciaResenhaDTO(autorID, alvoID, isRestaurante);

		return referencia;
	}
	@Transactional()
	public Resenha validarAutoria(Long resenhaID, long userID, String operacao) {

		Resenha resenha = repositorio.findById(resenhaID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Resenha não encontrado"));

		Usuario usuario = usuarioRepo.findById(userID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario não encontrado"));

		if (resenha.getAutor().getId() != usuario.getId())
			throw new AcessoNegadoException("Só o autor da resenha pode "+ operacao);

		return resenha;
	};

	@Transactional()
	public ResenhaResponse editar(Long resenhaID, ResenhaDTO dadosRequeridos, long userID) {
		
		Boolean editado = false;
		Resenha resenha = validarAutoria(resenhaID, userID,  "Edita-la");
		
		if (dadosRequeridos.titulo() != null && dadosRequeridos.titulo().isBlank() == false) {
			if (resenha.getTitulo().equals(dadosRequeridos.titulo()) == false) {

				resenha.setTitulo(dadosRequeridos.titulo());
				editado = true;
			}
		}
		
		if (dadosRequeridos.conteudo() != null && dadosRequeridos.conteudo().isBlank() == false) {
			if (resenha.getConteudo().equals(dadosRequeridos.conteudo()) == false) {

				resenha.setConteudo(dadosRequeridos.conteudo());
				editado = true;
			}
		}
		
		if(editado = false) throw new AcessoNegadoException("Nenhuma alteração foi realizada na resenha");
		
		resenha = repositorio.save(resenha);
		
		return resenha.toResponse();
	}

	@Transactional(readOnly = true)
	public Page<ResenhaResponse> retornarTodo(PageRequest request) {
		// TODO Auto-generated method stub
		return repositorio.findAll(request).map(x -> x.toResponse());
	}

	@Transactional(readOnly = true)
	public Page<ResenhaResponse> retornardeAlguem(PageRequest request, long usuarioID) {
		Page<Resenha>  response = repositorio.findAllByAutor_Id(usuarioID, request);
		return response.map(x-> x.toResponse());
	}

	@Transactional(readOnly = true)
	public Page<ResenhaResponse> retornardeItem(PageRequest request, Long itemID) {
		Page<Resenha>  response = repositorio.findAllByItem_Id(itemID, request);
		return response.map(x-> x.toResponse());
	}

	@Transactional(readOnly = true)
	public Page<ResenhaResponse> retornardeRestaurante(PageRequest request, Long restauID) {
		Page<Resenha>  response = repositorio.findAllByRestaurante_Id(restauID, request);
		return response.map(x-> x.toResponse());
	};

	
}
