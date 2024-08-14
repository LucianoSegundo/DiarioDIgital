package com.LSoftwareLTDA.diarioDigital.entidades;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "Tb_Titulo")
    private String titulo;
    
    @ManyToOne
    private Usuario usuario;

    @OneToMany
    @JoinColumn(name = "Tb_livro_Capitulo")
    private List<Capitulo> capitulos;

    public Livro(String titulo, Usuario usuario){
        this.capitulos = new ArrayList<Capitulo>();
        this.titulo = titulo;
        this.usuario = usuario;
    }
    public Livro() {};

	public Long getId() {
		return id;
	}

	public Usuario getUsuario() {
		return usuario;
	}
    
    

}
