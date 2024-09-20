package com.LSoftwareLTDA.diarioDigital.controller.dto.capitulos;

import com.LSoftwareLTDA.diarioDigital.entidades.Capitulo;

public record CapituloResponse(Long id, String titulo, String Conteudo, int numeroCapitulo) {

	public CapituloResponse(Capitulo entidade) {
		this(entidade.getId(), entidade.getTitulo(), entidade.getConteudo(), entidade.getNumeroCapitulo());
	}
}
