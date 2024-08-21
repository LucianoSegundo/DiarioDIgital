package com.LSoftwareLTDA.diarioDigital.entidades;

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

    @Column(name = "Tb_Conteudo")
    @NotBlank(message = "Campo conteudo não deve ser nulo")
    private String conteudo;
    
    @ManyToOne
    private Livro livro;

    public Capitulo(String titulo, String conteudo, Livro livro){
        this.titulo = titulo;
        this.conteudo= conteudo;
        this.livro= livro;

    }

}
