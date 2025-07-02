package com.LFSoftware.BroAvaliacao.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.LFSoftware.BroAvaliacao.Controladores.DTO.request.ComentarioDTO;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.response.ComentarioResponse;
import com.LFSoftware.BroAvaliacao.Servicos.ComentarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value = "api/v1/comentario")
public class ComentarioController {

	@Autowired
	private ComentarioService service;
	
	public ComentarioController() {}
	@Operation(summary = "comentar resenha ", description = "cria um comentario em uma resenha.")
	@ApiResponse(responseCode = "200", description = "criação bem sucedido")
	@ApiResponse(responseCode = "422", description = "criação negada devido a dados em branco.")
	@ApiResponse(responseCode = "404", description = "resenha não encontrada")
	@PostMapping(value = "/comentar/resenha/{resenhaID}")
	public ResponseEntity< ComentarioResponse> escreverComentarioResenha(JwtAuthenticationToken token, @RequestBody ComentarioDTO dto, @PathVariable Long resenhaID){
		
		ComentarioResponse response = service.comentarResenha(resenhaID, dto,  Long.parseLong(token.getName()));
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "comentar comentario ", description = "cria um comentario em uma comentario.")
	@ApiResponse(responseCode = "200", description = "criação bem sucedido")
	@ApiResponse(responseCode = "422", description = "criação negada devido a dados em branco.")
	@ApiResponse(responseCode = "404", description = "comentario não encontrada")
	@PostMapping(value = "/comentar/comentario/{comentarioID}")
	public ResponseEntity<ComentarioResponse> escreverComentarioEmComentario(JwtAuthenticationToken token, @RequestBody ComentarioDTO dto, @PathVariable Long comentarioID){
		
		ComentarioResponse response = service.comentarComentario(comentarioID, dto,  Long.parseLong(token.getName()));
		return ResponseEntity.ok(response);
	}
	
	
	@Operation(summary = "Excluir um comentario", description = "Exclui um comentario, Só é permitido caso o usuario seja aquele que escreveu o comentario.")
	@ApiResponse(responseCode = "200", description = "Excluir bem sucedida")
	@ApiResponse(responseCode = "402", description = "Usuario não é aquele que escreveu o comentario, então não pode exclui-lo")
	@ApiResponse(responseCode = "404", description = "usuario ou comentario não encontrados")
	@PutMapping(value = "/excluir/{comentarioID}")
	public ResponseEntity<Void> excluir(JwtAuthenticationToken token, @PathVariable Long comentarioID){
		service.excluir(comentarioID,Long.parseLong(token.getName()));
		
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Editar informações de um comentario", description = "Edita informações de um comentario, Só é permitido caso o usuario seja aquele que escreveu a comentario.")
	@ApiResponse(responseCode = "200", description = "Edição bem sucedida")
	@ApiResponse(responseCode = "402", description = "Usuario não é aquele que escreveu o comentario, então não pode editar suas informações.")
	@ApiResponse(responseCode = "404", description = "usuario ou comentario não encontrados")
	@PutMapping(value = "/editar/{comentarioID}")
	public ResponseEntity<ComentarioResponse> editar(JwtAuthenticationToken token ,@RequestBody ComentarioDTO dto, @PathVariable Long comentarioID){
		
		ComentarioResponse response = service.editar(comentarioID,dto,Long.parseLong(token.getName()));
		
		return ResponseEntity.ok(response);
	}
	
	
	@Operation(summary = "Listar comentarios de uma resenha", description = "Retorna uma lista paginada de comentarios de uma resenha")
	@ApiResponse(responseCode = "200", description = "operação bem sucedida")
	@ApiResponse(responseCode = "404", description = "Resenha não encontrados")
	@GetMapping(value = "/listar/resenha/{resenhaID}")
	public ResponseEntity<Page<ComentarioResponse>> listarComentarioResenha(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordarPor", defaultValue = "criacao") String ordarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem,
			 @PathVariable Long resenhaID,
			JwtAuthenticationToken token){
		PageRequest request = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordarPor);

		Page<ComentarioResponse> response = service.comentariosResenha(resenhaID, request);
		return ResponseEntity.ok(response);
	}	

	@Operation(summary = "Listar comentarios de um comentario", description = "Retorna uma lista paginada de comentarios de um comentario")
	@ApiResponse(responseCode = "200", description = "operação bem sucedida")
	@ApiResponse(responseCode = "404", description = "Comentario não encontrados")
	@GetMapping(value = "/listar/comentarios/{comentarioID}")
	public ResponseEntity<Page<ComentarioResponse>> ListarComentarioDeComentarios(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordarPor", defaultValue = "criacao") String ordarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem,
			 @PathVariable Long comentarioID,
			JwtAuthenticationToken token){
		PageRequest request = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordarPor);

		Page<ComentarioResponse> response = service.comentariosComentario(comentarioID, request);
		return ResponseEntity.ok(response);
	}

}
