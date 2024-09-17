package com.LSoftwareLTDA.diarioDigital.controller.dto.livro.response;

import com.LSoftwareLTDA.diarioDigital.entidades.Livro;

public record LivroResponse(Long id, String titulo, int NumeroCapitulos) {
	
	public LivroResponse(Livro livro) {
		this(livro.getId(), livro.getTitulo(), livro.getCapitulos().size());
	}

}
