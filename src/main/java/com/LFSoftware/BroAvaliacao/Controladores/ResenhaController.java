package com.LFSoftware.BroAvaliacao.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.LFSoftware.BroAvaliacao.Controladores.DTO.request.ReferenciaResenhaDTO;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.request.ResenhaDTO;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.response.ResenhaResponse;
import com.LFSoftware.BroAvaliacao.Servicos.ResenhaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value = "api/v1/resenha")
public class ResenhaController {

	@Autowired
	private ResenhaService service;

	public ResenhaController() {

	}

	@Operation(summary = "escrever resenha sobre restaurante", description = "Fornecer titulo e conteudo da resenha para escrevela em um restaurante.")
	@ApiResponse(responseCode = "200", description = "escrição bem sucedido")
	@ApiResponse(responseCode = "422", description = "escriçaõ negada devido a dados em branco.")
	@ApiResponse(responseCode = "404", description = "Restaurante ou usuario não encontrado")
	@PostMapping(value = "/escrever/restaurante/{restauID}")
	public ResponseEntity<ResenhaResponse> escreverParaRestaurante(JwtAuthenticationToken token,
			@RequestBody ResenhaDTO dto, @PathVariable Long restauID) {

		ResenhaResponse respponse = service.resenharRestaurante(restauID, dto, Long.parseLong(token.getName()));

		return ResponseEntity.ok(respponse);
	}

	@Operation(summary = "escrever resenha sobre Item", description = "Fornecer titulo e conteudo da resenha para escrevela em um Item.")
	@ApiResponse(responseCode = "200", description = "escrição bem sucedido")
	@ApiResponse(responseCode = "422", description = "escriçaõ negada devido a dados em branco.")
	@ApiResponse(responseCode = "404", description = "Item ou usuario não encontrado")
	@PostMapping(value = "/escrever/item/{itemID}")
	public ResponseEntity<ResenhaResponse> escreverParaItem(JwtAuthenticationToken token, @RequestBody ResenhaDTO dto,
			@PathVariable Long itemID) {

		ResenhaResponse respponse = service.resenharItem(itemID, dto, Long.parseLong(token.getName()));

		return ResponseEntity.ok(respponse);
	}

	@Operation(summary = "Consultar referencia de uma resenha", description = "Retorna o id do criador, do item ou do restaurante a quem a resenha se refere.")
	@ApiResponse(responseCode = "200", description = "retorno bem sucedida")
	@ApiResponse(responseCode = "402", description = "Usuario não é o criador da resenha, então não pode exclui-la.")
	@ApiResponse(responseCode = "404", description = "Resenha, usuario ou item não encontrados")
	@GetMapping(value = "/retornar/referencia/{resenhaID}")
	public ResponseEntity<ReferenciaResenhaDTO> retornarReferencia(JwtAuthenticationToken token,
			@PathVariable Long resenhaID) {

		ReferenciaResenhaDTO response = service.retornarReferencia(resenhaID);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Deletar uma resenha", description = "Exclui um resenha, e só é permitida a exclusão caso o usuário seja o criador da resenha.")
	@ApiResponse(responseCode = "200", description = "Exclusão bem sucedida")
	@ApiResponse(responseCode = "402", description = "Usuario não é o criador da resenha, então não pode exclui-la.")
	@ApiResponse(responseCode = "404", description = "Resenha ou usuario não encontrados")
	@DeleteMapping(value = "/deletar/{resenhaID}")
	public ResponseEntity<Void> deletar(JwtAuthenticationToken token, @PathVariable Long resenhaID) {

		service.deletar(resenhaID, Long.parseLong(token.getName()));
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Editar informações de um Resenha", description = "Edita informações de um Resenha, Só é permitido caso o usuario seja aquele que escreveu a resenha.")
	@ApiResponse(responseCode = "200", description = "Edição bem sucedida")
	@ApiResponse(responseCode = "402", description = "Usuario não é aquele que escreveu a resenha, então não pode editar suas informações.")
	@ApiResponse(responseCode = "404", description = "Resenha ou usuario não encontrados")
	@PutMapping(value = "/editar/{resenhaID}")
	public ResponseEntity<ResenhaResponse> editar(JwtAuthenticationToken token, @RequestBody ResenhaDTO dadosRequeridos,
			@PathVariable Long resenhaID) {

		ResenhaResponse response = service.editar(resenhaID, dadosRequeridos, Long.parseLong(token.getName()));

		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Listar resenhas de um restaurantes", description = "Retorna uma lista paginada de resenhas de restaurantes.")
	@ApiResponse(responseCode = "200", description = "operação bem sucedida")
	@ApiResponse(responseCode = "404", description = "Restaurante ou usuario não encontrados")
	@GetMapping(value = "/listar/restaurantes/{restauID}")
	public ResponseEntity<Page<ResenhaResponse>> listarResenhasRestaurantes(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordarPor", defaultValue = "criacao") String ordarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem, @PathVariable Long restauID,
			JwtAuthenticationToken token) {
		PageRequest request = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordarPor);

		Page<ResenhaResponse> response = service.retornardeRestaurante(request, restauID);

		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Listar resenhas de um Item", description = "Retorna uma lista paginada de resenhas de item.")
	@ApiResponse(responseCode = "200", description = "operação bem sucedida")
	@ApiResponse(responseCode = "404", description = "Restaurante ou usuario não encontrados")
	@GetMapping(value = "/listar/item/{itemID}")
	public ResponseEntity<Page<ResenhaResponse>> listarResenhaItem(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordarPor", defaultValue = "criacao") String ordarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem, @PathVariable Long itemID,
			JwtAuthenticationToken token) {
		PageRequest request = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordarPor);

		Page<ResenhaResponse> response = service.retornardeItem(request, itemID);

		return ResponseEntity.ok(response);
	}
 
	@Operation(summary = "Listar minhas resenhas", description = "Retorna lista paginada das minhas resenhas.")
	@ApiResponse(responseCode = "200", description = "operação bem sucedida")
	@ApiResponse(responseCode = "404", description = "Restaurante ou usuario não encontrados")
	@GetMapping(value = "/listar/minhas")
	public ResponseEntity<Page<ResenhaResponse>> listarMinhasResenhas(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordarPor", defaultValue = "criacao") String ordarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem, JwtAuthenticationToken token) {
		PageRequest request = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordarPor);

		Page<ResenhaResponse> response = service.retornardeAlguem(request, Long.parseLong(token.getName()));

		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Listar resenhas de outro usuario", description = "Retorna lista paginada de resenhas de outro usuario.")
	@ApiResponse(responseCode = "200", description = "operação bem sucedida")
	@ApiResponse(responseCode = "404", description = "Restaurante ou usuario não encontrados")
	@GetMapping(value = "/listar/usuario/{userID}")
	public ResponseEntity<Page<ResenhaResponse>> listarResenhasAlguem(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordarPor", defaultValue = "criacao") String ordarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem, @PathVariable Long userID,
			JwtAuthenticationToken token) {
		PageRequest request = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordarPor);

		Page<ResenhaResponse> response = service.retornardeAlguem(request, userID);

		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Listar todas as resenhas", description = "Retorna lista paginada detodas as resenhas.")
	@ApiResponse(responseCode = "200", description = "operação bem sucedida")
	@ApiResponse(responseCode = "404", description = "Restaurante ou usuario não encontrados")
	@GetMapping(value = "/listar")
	public ResponseEntity<Page<ResenhaResponse>> listarResenhas(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordarPor", defaultValue = "criacao") String ordarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem, JwtAuthenticationToken token) {
		PageRequest request = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordarPor);

		Page<ResenhaResponse> response = service.retornarTodo(request);

		return ResponseEntity.ok(response);
	}
}
