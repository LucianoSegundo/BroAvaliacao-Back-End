package com.LFSoftware.BroAvaliacao.Controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LFSoftware.BroAvaliacao.Controladores.DTO.CadastroRequest;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.LoginRequest;
import com.LFSoftware.BroAvaliacao.Controladores.DTO.LoginResponse;
import com.LFSoftware.BroAvaliacao.Servicos.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@CrossOrigin(origins  = "${url.do.cliente}" )
@RequestMapping(value = "/api/v1/usuario")
public class UsuarioController {
	
	UsuarioService userServi;

	
	public UsuarioController(UsuarioService userServi) {
		this.userServi = userServi;
		
	}

	
	@Operation(summary = "Rota Login de Usuário", description = "Forneça login e senha valido e receba o token de acesso.")
	@ApiResponse(responseCode = "200", description = "login bem sucedido")
	@ApiResponse(responseCode = "400", description = "Login mal sucedido")
	@ApiResponse(responseCode = "404", description = "O usuário fornecido não foi encontrado")
	@PostMapping(value = "/login")
	public ResponseEntity<LoginResponse> Login(@RequestBody LoginRequest request){
		
		LoginResponse response = userServi.Login(request);
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Rota Cadastro de Usuário", description = "Recebe String nome, String senha, String Email retorna o o nome do usuário  por formalidade. O email deve seguir a e regex ^[A-Za-z0-9+_.-]+@(.+)$. Enquanto a senha deve conter no minimo 8 numeros, no minimo 1 numero e no minimo 1 caracter especial ")
	@ApiResponse(responseCode = "200", description = "cadastro bem sucedido")
	@ApiResponse(responseCode = "422", description = "o cadastro foi negado, um dos possiveis motivos é retornado como mensagem")
	@ApiResponse(responseCode = "500", description = "Os papeis não estão cadastrados no banco de dados, então não puderam ser recuperados para a realização do cadastro.")

	@PostMapping(value = "/")
	public ResponseEntity<LoginResponse> cadastro(@RequestBody CadastroRequest request){
		
		LoginResponse response = userServi.cadastroUsuario(request);
		
		return ResponseEntity.ok(response);
	}
	
}
