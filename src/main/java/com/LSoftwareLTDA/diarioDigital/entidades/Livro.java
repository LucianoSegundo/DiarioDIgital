package com.LSoftwareLTDA.diarioDigital.entidades;

import java.util.ArrayList;
import java.util.List;

import com.LSoftwareLTDA.diarioDigital.controller.dto.livro.response.LivroResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Livro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "Tb_Titulo")
	@NotBlank(message = "Titulo de usuário não pode ser nulo")
	private String titulo;

	@ManyToOne
	@JoinColumn(name = "usuario_Id")
	@JsonIgnore
	private Usuario usuario;

	@OneToMany(mappedBy = "livro", cascade = CascadeType.ALL)
	private List<Capitulo> capitulos;

	public Livro() {
	};

	public Livro(String titulo, Usuario usuario) {
		this.titulo = titulo;
		this.usuario = usuario;
		this.capitulos = new ArrayList<>();
	}

	public Livro(LivroResponse entidade) {
		this.id = entidade.id();
		this.titulo = entidade.titulo();
		this.capitulos = new ArrayList<Capitulo>();
	}

	public Long getId() {
		return id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<Capitulo> getCapitulos() {
		return capitulos;
	}

	public void setCapitulos(List<Capitulo> capitulos) {
		this.capitulos = capitulos;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
