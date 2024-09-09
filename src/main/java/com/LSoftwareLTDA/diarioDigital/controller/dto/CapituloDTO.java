package com.LSoftwareLTDA.diarioDigital.controller.dto;

import com.LSoftwareLTDA.diarioDigital.entidades.Capitulo;
import com.LSoftwareLTDA.diarioDigital.entidades.Livro;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CapituloDTO {

	private Long id;
    private String titulo;
    private int numeroCapitulo;
    private String conteudo;
    
    private Long idLivro;
    private Long idUsuario;
    
    @JsonIgnore
    private Livro livro;

    public CapituloDTO(Capitulo capitulo) {
    	this.titulo = capitulo.getTitulo();
    	this.conteudo = capitulo.getConteudo();
    	this.id = capitulo.getId();
    	this.livro = capitulo.getLivro();
    	this.numeroCapitulo = capitulo.getNumeroCapitulo();
    }
    public CapituloDTO(String titulo, String conteudo, int numero, Long livroid, Long userid){
        this.titulo = titulo;
        this.conteudo= conteudo;
        this.idLivro = livroid;
        this.idUsuario= userid;
        this.numeroCapitulo = numero;

    }
	public CapituloDTO() {
	}
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
	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	public Livro getLivro() {
		return livro;
	}
	public void setLivro(Livro livro) {
		this.livro = livro;
	}
	public int getNumeroCapitulo() {
		return numeroCapitulo;
	}
	public void setNumeroCapitulo(int numeroCapitulo) {
		this.numeroCapitulo = numeroCapitulo;
	}
	public Long getIdLivro() {
		return idLivro;
	}
	public void setIdLivro(Long livroid) {
		this.idLivro = livroid;
	}
	public Long getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Long usuarioJD) {
		this.idUsuario = usuarioJD;
	}
	
    

}
