package com.LSoftwareLTDA.diarioDigital.controller.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.LSoftwareLTDA.diarioDigital.service.excecoes.CadastroNegadoException;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.EntidadeNaoEncontrada;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.PermissaoNegadaException;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.TokenInvalido;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHendler {

	@ExceptionHandler(EntidadeNaoEncontrada.class)
	public ResponseEntity<ErroPadrao> entidadeNaoEncontrada(EntidadeNaoEncontrada e, HttpServletRequest request){
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		ErroPadrao erro =new ErroPadrao(
				Instant.now(),
				status.value(),
				e.getMessage(),
				request.getRequestURI() );
		
		return ResponseEntity.status(status).body(erro);
		
	}
	
	@ExceptionHandler(CadastroNegadoException.class)
	public ResponseEntity<ErroPadrao> cadastroNegado(CadastroNegadoException e, HttpServletRequest request){
		
		HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
		ErroPadrao erro = new ErroPadrao(
				Instant.now(),
				status.value(),
				e.getMessage(),
				request.getRequestURI());
		
		return ResponseEntity.status(status).body(erro);

	}
	
	@ExceptionHandler(PermissaoNegadaException.class)
	public ResponseEntity<ErroPadrao> permissaoNegada(PermissaoNegadaException e, HttpServletRequest request){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ErroPadrao erro = new ErroPadrao(
				Instant.now(),
				status.value(),
				e.getMessage(),
				request.getRequestURI());
		
		return ResponseEntity.status(status).body(erro);

	}
	
	@ExceptionHandler(TokenInvalido.class)
	public ResponseEntity<ErroPadrao> tokenInvalido(TokenInvalido e, HttpServletRequest request){
		
		HttpStatus status = HttpStatus.FORBIDDEN;
		ErroPadrao erro = new ErroPadrao(
				Instant.now(),
				status.value(),
				e.getMessage(),
				request.getRequestURI());
		
		return ResponseEntity.status(status).body(erro);

	}
}
