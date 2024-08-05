package com.LSoftwareLTDA.diarioDigital.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.LSoftwareLTDA.diarioDigital.entidades.Livro;
import com.LSoftwareLTDA.diarioDigital.repositorios.UsuarioRepositorio;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@NoArgsConstructor
public class LivrosService {

    private UsuarioRepositorio userRepo;

    public LivrosService(UsuarioRepositorio repositorio){
        this.userRepo = repositorio;

    }
    public Boolean criarLivro(String titulo){

     return false;   
    };
    public Boolean excluir(Long id){

        return false;   
    };
    public List<Livro> listar(String titulo){

        return null;   
    };

}
