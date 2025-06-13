package com.LFSoftware.BroAvaliacao.Controladores;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.LFSoftware.BroAvaliacao.Controladores.DTO.RestauranteDTO;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.RestauranteResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value = "api/v1/restaurante")
public class RestauranteController {

	
	@Operation(summary = "criar restaurante", description = "Definir nome, e endereço do restaurante, além de preencher se é ou não o proprietário do restaurante.")
	@ApiResponse(responseCode = "200", description = "criação bem sucedido, ao se marcar como proprietario é adiquirido o role de proprietário")
	@ApiResponse(responseCode = "422", description = "criação negada devido a dados em branco.")
	@PostMapping(value = "/criar")
	public ResponseEntity<RestauranteResponse> criar(JwtAuthenticationToken token, @RequestBody RestauranteDTO dto, Boolean isProprietario){
		
		
		return null;
	}
	
	@Operation(summary = "Deletar um restaurante", description = "Exclui um restaurante, metodo restrito a usuarios com o role proprietário, e só é permitida a exclusão caso o usuário seja o proprietário do restaurante.")
	@ApiResponse(responseCode = "200", description = "Exclusão bem sucedida")
	@ApiResponse(responseCode = "402", description = "Usuario não é o proprietário do restaurante, então não pode exclui-lo.")
	@ApiResponse(responseCode = "404", description = "Restaurante ou proprietário não encontrados")
	@PreAuthorize("hasAuthority('SCOPE_proprietario')")
	@DeleteMapping(value = "/deletar/{restauID}")
	public ResponseEntity<Void> deletar(JwtAuthenticationToken token, @PathVariable Long restauID){
		
		
		return null;
	}
	
	@Operation(summary = "ocultar ou exibir um restaurante", description = "Oculta um restaurante, metodo restrito a usuarios com o role proprietário, e só é permitida a exclusão caso o usuário seja o proprietário do restaurante.")
	@ApiResponse(responseCode = "200", description = "Ocultação bem sucedida")
	@ApiResponse(responseCode = "402", description = "Usuario não é o proprietário do restaurante, então não pode oculta-lo.")
	@ApiResponse(responseCode = "404", description = "Restaurante ou proprietário não encontrados")
	@PreAuthorize("hasAuthority('SCOPE_proprietario')")
	@PutMapping(value = "/ocultar/{restauID}")
	public ResponseEntity<Void> ocultar(JwtAuthenticationToken token, @PathVariable Long restauID){
		
		
		return null;
	}
	
	@Operation(summary = "Editar informações de um restaurante", description = "Edita informações de um restaurante, caso o restaurante tenha um proprietário só ele podera editar as informações do restaurante.")
	@ApiResponse(responseCode = "200", description = "Edição bem sucedida")
	@ApiResponse(responseCode = "402", description = "Usuario não é o proprietário do restaurante, então não pode editar suas informações.")
	@ApiResponse(responseCode = "404", description = "Restaurante ou proprietário não encontrados")
	@PutMapping(value = "/editar/{restauID}")
	public ResponseEntity<RestauranteResponse> editar(JwtAuthenticationToken token ,@RequestBody RestauranteDTO dto, @PathVariable Long restauID){
		
		
		return null;
	}
	
	@Operation(summary = "Listar restaurantes", description = "Retorna uma lista paginada de restaurantes.")
	@ApiResponse(responseCode = "200", description = "operação bem sucedida")
	@ApiResponse(responseCode = "404", description = "Restaurante ou proprietário não encontrados")
	@GetMapping(value = "/listar")
	public ResponseEntity<Page<RestauranteResponse>> listar(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordarPor", defaultValue = "nome") String ordarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem,
			JwtAuthenticationToken token){
		PageRequest request = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordarPor);

		
		return null;
	}
	
	@Operation(summary = "Listar meus restaurantes", description = "Retorna uma lista paginada de restaurantes. metodo restrito a proprietários")
	@ApiResponse(responseCode = "200", description = "operação bem sucedida")
	@ApiResponse(responseCode = "404", description = "Restaurante ou proprietário não encontrados")
	@PreAuthorize("hasAuthority('SCOPE_proprietario')")
	@GetMapping(value = "/listar/meus")
	public ResponseEntity<Page<RestauranteResponse>> listarMeusRestaurantes(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordarPor", defaultValue = "nome") String ordarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem,
			JwtAuthenticationToken token){
		
		PageRequest request = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordarPor);

		
		return null;
	}
	
	
}
