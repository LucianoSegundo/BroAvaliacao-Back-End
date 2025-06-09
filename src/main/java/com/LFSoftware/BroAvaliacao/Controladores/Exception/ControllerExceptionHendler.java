package com.LFSoftware.BroAvaliacao.Controladores.Exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.LFSoftware.BroAvaliacao.Excecoes.AcessoNegadoException;
import com.LFSoftware.BroAvaliacao.Excecoes.AusendiaDadosException;
import com.LFSoftware.BroAvaliacao.Excecoes.CadastroNedadoException;
import com.LFSoftware.BroAvaliacao.Excecoes.EntidadeNaoEncontradaException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHendler {

	@ExceptionHandler(AcessoNegadoException.class)
	public ResponseEntity<ErroPadrao> acessoNegado(AcessoNegadoException e, HttpServletRequest request){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ErroPadrao erro =new ErroPadrao(
				Instant.now(),
				status.value(),
				e.getMessage(),
				request.getRequestURI() );
		
		return ResponseEntity.status(status).body(erro);
		
	}
	
	@ExceptionHandler(CadastroNedadoException.class)
	public ResponseEntity<ErroPadrao> CadastroNegado(CadastroNedadoException e, HttpServletRequest request){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ErroPadrao erro =new ErroPadrao(
				Instant.now(),
				status.value(),
				e.getMessage(),
				request.getRequestURI() );
		
		return ResponseEntity.status(status).body(erro);
		
	}
	
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<ErroPadrao> entidadeNaoEncontrada(EntidadeNaoEncontradaException e, HttpServletRequest request){
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		ErroPadrao erro =new ErroPadrao(
				Instant.now(),
				status.value(),
				e.getMessage(),
				request.getRequestURI() );
		
		return ResponseEntity.status(status).body(erro);
		
	}
	
	@ExceptionHandler(AusendiaDadosException.class)
	public ResponseEntity<ErroPadrao> dadosEssenciasfaltando(AusendiaDadosException e, HttpServletRequest request){
		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ErroPadrao erro =new ErroPadrao(
				Instant.now(),
				status.value(),
				e.getMessage(),
				request.getRequestURI() );
		
		return ResponseEntity.status(status).body(erro);
		
	}

	

	
	
	
	
}
	
