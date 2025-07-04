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

import com.LFSoftware.BroAvaliacao.Controladores.DTO.request.ItemAlteradoDTO;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.request.ItemDTO;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.response.ItemResponse;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.response.LogResponse;
import com.LFSoftware.BroAvaliacao.Servicos.ItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value = "api/v1/restaurante/{restauID}/item")
public class ItemController {

	@Autowired
	private ItemService service;

	public ItemController() {

	}

	@Operation(summary = "criar item", description = "Forneça o nome e a descrição do item, caso o restaurante tenha um proprietario, só ele pode adicionar itens.")
	@ApiResponse(responseCode = "200", description = "criação bem sucedido")
	@ApiResponse(responseCode = "422", description = "criação negada devido a dados em branco.")
	@ApiResponse(responseCode = "404", description = "proprietario ou restaurante não encontrados.")
	@PostMapping(value = "/criar")
	public ResponseEntity<ItemResponse> criar(JwtAuthenticationToken token, @RequestBody ItemDTO dadosRequeridos,
			@PathVariable Long restauID) {

		ItemResponse resposta = service.criar(restauID, dadosRequeridos, Long.parseLong(token.getName()));

		return ResponseEntity.ok(resposta);
	}

	@Operation(summary = "Deletar um item", description = "Exclui um item, metodo restrito a usuarios com o role proprietário, e só é permitida a exclusão caso o usuário seja o proprietário do restaurante a qual o item pertence.")
	@ApiResponse(responseCode = "200", description = "Exclusão bem sucedida")
	@ApiResponse(responseCode = "402", description = "Usuario não é o proprietário do restaurante ao qual o item está vinculado, então não pode exclui-lo.")
	@ApiResponse(responseCode = "404", description = "Restaurante, proprietário ou item não encontrados")
	@DeleteMapping(value = "/deletar/{itemID}")
	public ResponseEntity<Void> deletar(JwtAuthenticationToken token, @PathVariable Long restauID,
			@PathVariable Long itemID,
			@RequestBody @Schema(type = "string", example = "texto justificando a ação tomada.") String justificativa) {

		service.deletar(itemID, restauID, justificativa, Long.parseLong(token.getName()));

		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Editar informações de um Item", description = "Edita informações de um Item, caso o restaurante tenha um proprietário só ele podera editar as informações dos itens vinculados aos restaurante.")
	@ApiResponse(responseCode = "200", description = "Edição bem sucedida")
	@ApiResponse(responseCode = "402", description = "Usuario não é o proprietário do restaurante, então não pode editar suas informações.")
	@ApiResponse(responseCode = "404", description = "Restaurante ou proprietário não encontrados")
	@PutMapping(value = "/editar/{itemID}")
	public ResponseEntity<ItemResponse> editar(JwtAuthenticationToken token, @PathVariable Long restauID,
			@PathVariable Long itemID, @RequestBody ItemAlteradoDTO dadosRequeridos) {

		ItemResponse resposta = service.editar(itemID, restauID, dadosRequeridos, Long.parseLong(token.getName()));
		return ResponseEntity.ok(resposta);

	}

	@Operation(summary = "Listar itens", description = "Retorna uma lista paginada de itens de um restaurante.")
	@ApiResponse(responseCode = "200", description = "operação bem sucedida")
	@ApiResponse(responseCode = "404", description = "Restaurante ou proprietário não encontrados")
	@GetMapping(value = "/listar")
	public ResponseEntity<Page<ItemResponse>> listar(@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordarPor", defaultValue = "nome") String ordarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem, @PathVariable Long restauID,
			JwtAuthenticationToken token) {
		PageRequest request = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordarPor);

		Page<ItemResponse> resposta = service.listar(restauID, request);
		return ResponseEntity.ok(resposta);
	}

	@Operation(summary = "Listar restaurantes", description = "Retorna uma lista paginada de restaurantes.")
	@ApiResponse(responseCode = "200", description = "operação bem sucedida")
	@ApiResponse(responseCode = "404", description = "Restaurante ou proprietário não encontrados")
	@GetMapping(value = "/{itemID}/log")
	public ResponseEntity<Page<LogResponse>> listarLog(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordarPor", defaultValue = "dataCriacao") String ordarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem, JwtAuthenticationToken token,
			@PathVariable Long itemID) {
		PageRequest request = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordarPor);

		Page<LogResponse> resposta = service.retornarLogitem(itemID, request);
		return ResponseEntity.ok(resposta);
	}

}
