package com.LSoftwareLTDA.diarioDigital.controller.dto.usuario.resposta;

import com.LSoftwareLTDA.diarioDigital.entidades.Usuario;

public record UsuarioResponse(Long id, String nome, int idade) {

	public UsuarioResponse(Usuario entidade) {
		this( entidade.getId(),entidade.getNome(),entidade.getIdade());
		
	}
}
