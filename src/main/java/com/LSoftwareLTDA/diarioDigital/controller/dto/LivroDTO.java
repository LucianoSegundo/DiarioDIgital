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
        this.capitulos = new ArrayList<Capitulo>();
        this.titulo = titulo;
        this.usuario = usuario;
    }
    public LivroDTO(Livro entidade){
        this.capitulos = entidade.getCapitulos();
        this.titulo = entidade.getTitulo();
        this.usuario = entidade.getUsuario();
        this.id = entidade.getId();
    }
    public LivroDTO() {};

}
