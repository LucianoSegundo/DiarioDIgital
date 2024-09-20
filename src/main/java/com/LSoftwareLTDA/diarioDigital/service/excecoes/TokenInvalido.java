package com.LSoftwareLTDA.diarioDigital.service.excecoes;

public class TokenInvalido extends RuntimeException {

	public TokenInvalido(String mensagem) {
		super(mensagem);
	}

}
