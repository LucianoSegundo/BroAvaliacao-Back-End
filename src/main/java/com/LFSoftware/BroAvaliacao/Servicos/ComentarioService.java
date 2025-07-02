package com.LFSoftware.BroAvaliacao.Servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.LFSoftware.BroAvaliacao.Controladores.DTO.request.ComentarioDTO;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.response.ComentarioResponse;
import com.LFSoftware.BroAvaliacao.Entidade.Comentario;
import com.LFSoftware.BroAvaliacao.Entidade.Resenha;
import com.LFSoftware.BroAvaliacao.Entidade.Usuario;
import com.LFSoftware.BroAvaliacao.Excecoes.AcessoNegadoException;
import com.LFSoftware.BroAvaliacao.Excecoes.AusendiaDadosException;
import com.LFSoftware.BroAvaliacao.Excecoes.EntidadeNaoEncontradaException;
import com.LFSoftware.BroAvaliacao.Repositorio.ComentarioRepository;
import com.LFSoftware.BroAvaliacao.Repositorio.ResenhaRepository;
import com.LFSoftware.BroAvaliacao.Repositorio.UsuarioRepository;

@Service
public class ComentarioService {

	@Autowired
	private ComentarioRepository repositorio;
	@Autowired
	private ResenhaRepository resenhaRepo;
	@Autowired
	private UsuarioRepository usuarioRepo;

	public ComentarioService() {
	}

	@Transactional()
	public ComentarioResponse comentarResenha(Long resenhaID, ComentarioDTO dadosRequeridos, long userID) {

		Comentario entidade = comentar(dadosRequeridos, userID);

		Resenha resenha = resenhaRepo.findById(resenhaID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Resenha não encontrado"));

		entidade.setResenha(resenha);
		resenha.getComentarios().add(entidade);

		Comentario response = repositorio.save(entidade);
		return response.toResponse();
	}

	@Transactional()
	public ComentarioResponse comentarComentario(Long comentarioID, ComentarioDTO dadosRequeridos, long userID) {

		Comentario entidade = comentar(dadosRequeridos, userID);

		Comentario comentarioPai = repositorio.findById(comentarioID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Comentario não encontrado"));

		entidade.setComentariopai(comentarioPai);
		comentarioPai.getComentarios().add(entidade);

		Comentario response = repositorio.save(entidade);
		return response.toResponse();
	}

	@Transactional(readOnly = true)
	public Comentario comentar(ComentarioDTO dadosRequeridos, long userID) {
		if (dadosRequeridos.mensagem() == null || dadosRequeridos.mensagem().isBlank())
			throw new AusendiaDadosException("A mensagem do comentario não pode ser nulo.");

		Usuario usuario = usuarioRepo.findById(userID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario não encontrado"));

		Comentario entidade = new Comentario();

		entidade.setMensagem(dadosRequeridos.mensagem());
		entidade.setAutor(usuario);
		entidade.setNumeroComentarios(0);
		usuario.getComentarios().add(entidade);

		return entidade;

	}

	@Transactional()
	public void excluir(Long comentarioID, long userID) {

		Comentario comentario = validarAutorio(comentarioID, userID, "excluir");

		repositorio.deleteById(comentario.getId());
	}

	@Transactional()
	public ComentarioResponse editar(Long comentarioID, ComentarioDTO dadosRequeridos, long userID) {
		if (dadosRequeridos.mensagem() == null || dadosRequeridos.mensagem().isBlank())
			throw new AusendiaDadosException("Nenhuma informação foi alterada, campo mensagem vazio.");

		Comentario comentario = validarAutorio(comentarioID, userID, "editar");

		comentario.setMensagem(dadosRequeridos.mensagem());

		comentario = repositorio.save(comentario);

		return comentario.toResponse();
	}

	@Transactional(readOnly = true)
	public Comentario validarAutorio(Long comentarioID, long userID, String mensagem) {
		Comentario comentario = repositorio.findById(comentarioID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Comentario não encontrado"));

		Usuario usuario = usuarioRepo.findById(userID)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario não encontrado"));

		if (comentario.getAutor().getId() != usuario.getId())
			throw new AcessoNegadoException("Só o autor pode " + mensagem + " o comentario.");

		return comentario;
	}

	@Transactional(readOnly = true)
	public Page<ComentarioResponse> comentariosResenha(Long resenhaID, PageRequest request) {

		Page<Comentario> response = repositorio.findAllByResenha_Id(request, resenhaID);

		return response.map(x -> x.toResponse());
	}

	@Transactional(readOnly = true)
	public Page<ComentarioResponse> comentariosComentario(Long comentarioID, PageRequest request) {

		Page<Comentario> response = repositorio.findAllByComentariopai_Id(request, comentarioID);

		return response.map(x -> x.toResponse());
	}

}
