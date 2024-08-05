package com.LSoftwareLTDA.diarioDigital.service;

import java.util.List;

import com.LSoftwareLTDA.diarioDigital.entidades.Capitulo;
import com.LSoftwareLTDA.diarioDigital.repositorios.UsuarioRepositorio;

public class CapituloService {

     private UsuarioRepositorio userRepo;

    public LivrosService(UsuarioRepositorio repositorio){
        this.userRepo = repositorio;

    }

    public Boolean criarCapitulo(String titulo, String conteudo, Long idLivro ){

     return false;   
    };
    public Boolean excluirCapitulo(Long id){

        return false;   
    };
    public List<Capitulo> listarCapitulo(Long idLivro){

        return null;   
    };
    public Boolean moverCapitulo(Long livroAtual, Long livroNovo){

        return false;
    };


}
