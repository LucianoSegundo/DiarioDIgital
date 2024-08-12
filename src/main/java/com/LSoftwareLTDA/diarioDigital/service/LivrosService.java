package com.LSoftwareLTDA.diarioDigital.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.LSoftwareLTDA.diarioDigital.entidades.Livro;
import com.LSoftwareLTDA.diarioDigital.entidades.Usuario;
import com.LSoftwareLTDA.diarioDigital.repositorios.LivroRepositorio;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@NoArgsConstructor
public class LivrosService {

    private LivroRepositorio livroRepo;

    public LivrosService(LivroRepositorio repositorio){
        this.livroRepo = repositorio;

    }
    public Livro criarLivro(String titulo, UUID idUsuario){

     return null;   
    };
    public Livro consultarLivro(String titulo, UUID idUsuario){

        return null;   
       };
    public Boolean excluirLivro(Long id, UUID idUsuario, String senha){

        return false;   
    };
    public List<Livro> listarLivros(UUID idUsuario){

        return null;   
    };

}
