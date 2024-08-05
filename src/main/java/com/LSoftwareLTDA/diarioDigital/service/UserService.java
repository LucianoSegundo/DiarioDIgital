package com.LSoftwareLTDA.diarioDigital.service;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.LSoftwareLTDA.diarioDigital.entidades.Usuario;
import com.LSoftwareLTDA.diarioDigital.interfaces.RecuperacaoSenha;
import com.LSoftwareLTDA.diarioDigital.repositorios.UsuarioRepositorio;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@NoArgsConstructor
@Getter
@Setter
public class UserService {

    private UsuarioRepositorio userRepo;
    private RecuperacaoSenha recuperadorSenha;

    public UserService(UsuarioRepositorio repositorio, RecuperacaoSenha recuperador){
        this.userRepo = repositorio;
        this.recuperadorSenha = recuperador;

    }

    public Boolean cadastrar(String nome,String senha){

    return false;
    };

    public Boolean excluir(UUID id) {

    return false;    
    };

    public Usuario procurar(UUID id){

     return null;   
    };

    public Boolean logar(String nome, String senha){

    return false;
    };
    public Boolean trocarSenha(String senha){
        
    return false;
    };


}
