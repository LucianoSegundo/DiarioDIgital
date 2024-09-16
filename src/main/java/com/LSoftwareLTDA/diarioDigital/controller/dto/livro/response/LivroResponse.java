package com.LSoftwareLTDA.diarioDigital.controller.dto.livro.response;

import com.LSoftwareLTDA.diarioDigital.entidades.Livro;

public record LivroResponse(Long id, String titulo) {
	
	public LivroResponse(Livro livro) {
		this(livro.getId(), livro.getTitulo());
	}

}
