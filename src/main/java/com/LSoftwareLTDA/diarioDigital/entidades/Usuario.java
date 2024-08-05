package com.LSoftwareLTDA.diarioDigital.entidades;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy =GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Campo não deve ser nulo")
    private String nome;

    @NotBlank(message = "Campo não deve ser nulo")
    private String senha;

    private Boolean ativo;

    @OneToMany
    private List<Livro> livros;

    public Usuario(String nome, String senha){
        this.livros
    }

}
