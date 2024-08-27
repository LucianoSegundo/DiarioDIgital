package com.LSoftwareLTDA.diarioDigital.entidades;

import com.LSoftwareLTDA.diarioDigital.controller.dto.CapituloDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Capitulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Tb_Titulo")
    private String titulo;
    
    @Column(name = "Tb_Numero_Capitulo")
    private int numeroCapitulo;

    @Column(name = "Tb_Conteudo")
    @NotBlank(message = "Campo conteudo não deve ser nulo")
    private String conteudo;
    
    @ManyToOne
    @JsonIgnore
    private Livro livro;
    
    
    public Capitulo() {};
    public Capitulo(CapituloDTO capitulo, Livro livro) {
    	this.titulo = capitulo.getTitulo();
    	this.conteudo = capitulo.getConteudo();
    	this.livro = livro;
    	this.numeroCapitulo = capitulo.getNumeroCapitulo();

    }
    public Capitulo(CapituloDTO capitulo) {
    	this.titulo = capitulo.getTitulo();
    	this.conteudo = capitulo.getConteudo();
    	this.id = capitulo.getId();
    	this.livro = capitulo.getLivro();
    	this.numeroCapitulo = capitulo.getNumeroCapitulo();

    }
    public Capitulo(String titulo, String conteudo, Livro livro, int numero){
        this.titulo = titulo;
        this.conteudo= conteudo;
        this.livro= livro;
        this.numeroCapitulo = numero;
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
    
    

}
