package com.LSoftwareLTDA.diarioDigital.controller.dto;

import java.util.ArrayList;
import java.util.List;

import com.LSoftwareLTDA.diarioDigital.entidades.Capitulo;
import com.LSoftwareLTDA.diarioDigital.entidades.Livro;
import com.LSoftwareLTDA.diarioDigital.entidades.Usuario;

public class LivroDTO {

   private Long id;

   private String titulo;
    
   private Usuario usuario;

   private List<Capitulo> capitulos;

    public LivroDTO(String titulo, Usuario usuario){
        this.titulo = titulo;
        this.usuario = usuario;
    }
    public LivroDTO(Livro entidade){
        this.capitulos = entidade.getCapitulos();
        this.titulo = entidade.getTitulo();
        this.usuario = entidade.getUsuario();
        this.id = entidade.getId();
    }
    public LivroDTO() {}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public List<Capitulo> getCapitulos() {
		return capitulos;
	}
	public void setCapitulos(List<Capitulo> capitulos) {
		this.capitulos = capitulos;
	};

}
