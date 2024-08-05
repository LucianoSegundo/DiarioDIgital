package com.LSoftwareLTDA.diarioDigital.entidades;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "Tb_Titulo")
    private String titulo;

    @OneToMany
    @JoinColumn(name = "Tb_livro-Capitulo")
    private List<Capitulo> capitulos;

    public Livro(String titulo){
        this.capitulos = new ArrayList<Capitulo>();
        this.titulo = titulo;
    }

    

}
