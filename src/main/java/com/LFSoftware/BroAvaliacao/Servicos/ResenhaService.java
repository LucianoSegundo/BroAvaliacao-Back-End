package com.LFSoftware.BroAvaliacao.Servicos;

import org.springframework.beans.factory.annotation.Autowired;

import com.LFSoftware.BroAvaliacao.Controladores.DTO.request.ResenhaDTO;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.response.ResenhaResponse;
import com.LFSoftware.BroAvaliacao.Entidade.Item;
import com.LFSoftware.BroAvaliacao.Entidade.Resenha;
import com.LFSoftware.BroAvaliacao.Entidade.Restaurante;
import com.LFSoftware.BroAvaliacao.Entidade.Usuario;
import com.LFSoftware.BroAvaliacao.Excecoes.AusendiaDadosException;
import com.LFSoftware.BroAvaliacao.Excecoes.EntidadeNaoEncontradaException;
import com.LFSoftware.BroAvaliacao.Repositorio.ItemRepository;
import com.LFSoftware.BroAvaliacao.Repositorio.ResenhaRepository;
import com.LFSoftware.BroAvaliacao.Repositorio.RestauranteRepository;
import com.LFSoftware.BroAvaliacao.Repositorio.UsuarioRepository;

public class ResenhaService {

	@Autowired
	private ResenhaRepository repositorio;
	@Autowired
	private UsuarioRepository usuarioRepo;
	@Autowired
	private RestauranteRepository restRepo;
	@Autowired
	private ItemRepository itemRepo;
	
	public ResenhaService(){
		
	}
	
	public ResenhaResponse resenharRestaurante(Long restauID, ResenhaDTO dadosRequeridos, long userID) {

		Resenha entidade = criarResenha(userID, dadosRequeridos);
		
		Restaurante restaurante = restRepo.findById(restauID).orElseThrow(() -> new EntidadeNaoEncontradaException("Restaurante não encontrado"));

		entidade.setRestaurante(restaurante);
		restaurante.getResenhas().add(entidade);
		
		return persistirResenha(entidade);
	}
	
	public ResenhaResponse resenharItem(Long itemID, ResenhaDTO dadosRequeridos, long userID) {

		Resenha entidade = criarResenha(userID, dadosRequeridos);
		
		Item item = itemRepo.findById(itemID).orElseThrow(() -> new EntidadeNaoEncontradaException("Item não encontrado"));

		entidade.setItem(item);
		item.getResenhas().add(entidade);
		
		return persistirResenha(entidade);
	}
	
	public Resenha criarResenha(Long userID, ResenhaDTO dadosRequeridos) {
		if(dadosRequeridos.conteudo() == null || dadosRequeridos.conteudo().isBlank() ) throw new AusendiaDadosException("O conteudo da postagem não pode ser nulo.");
		if(dadosRequeridos.titulo() == null || dadosRequeridos.titulo().isBlank() ) throw new AusendiaDadosException("O titulo da postagem não pode ser nulo.");

		Usuario usuario = usuarioRepo.findById(userID).orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario não encontrado"));
				
		Resenha entidade = new Resenha();
		
		entidade.setTitulo(dadosRequeridos.titulo());
		entidade.setConteudo(dadosRequeridos.conteudo());
		entidade.setAutor(usuario);
		
		return entidade;
	}
	
	public ResenhaResponse persistirResenha(Resenha resenha) {
		
		return repositorio.save(resenha).toResponse();
	};
}
