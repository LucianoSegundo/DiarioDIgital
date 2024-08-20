package com.LSoftwareLTDA.diarioDigital.service;

import org.springframework.stereotype.Component;

import com.LSoftwareLTDA.diarioDigital.interfaces.RecuperacaoSenha;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@NoArgsConstructor
@Getter
@Setter
public class RecuperarPorPalavra implements RecuperacaoSenha {

	@Override
	public Boolean trocar(String novasenha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean verificar() {
		// TODO Auto-generated method stub
		return null;
	}

}
