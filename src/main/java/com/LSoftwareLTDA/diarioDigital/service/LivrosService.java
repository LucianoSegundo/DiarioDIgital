package com.LSoftwareLTDA.diarioDigital.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.LSoftwareLTDA.diarioDigital.entidades.Livro;
import com.LSoftwareLTDA.diarioDigital.excecoes.GerenciamentoLivroException;
import com.LSoftwareLTDA.diarioDigital.excecoes.GerenciamentoUsuariosException;
import com.LSoftwareLTDA.diarioDigital.repositorios.LivroRepositorio;
import com.LSoftwareLTDA.diarioDigital.repositorios.UsuarioRepositorio;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@NoArgsConstructor
public class LivrosService {

    private LivroRepositorio livroRepo;
    private UsuarioRepositorio userRepo;

    public LivrosService(LivroRepositorio repositorio, UsuarioRepositorio userRepo){
        this.livroRepo = repositorio;
        this.userRepo =userRepo;

    }
    @Transactional
    public Livro criarLivro(String titulo, UUID idUsuario) throws Exception{
    	
    	if(idUsuario == null || titulo == null) throw new NullPointerException("Não é permitivo criar livros com atributos nulos");
    	
    		var usuario = userRepo.findById(idUsuario);
    		if(usuario.isEmpty()) throw new GerenciamentoUsuariosException("Usuario não foi cadastrado");
    		
    		var  livro = livroRepo.findByTituloAndUsuario_id(titulo, idUsuario);
    		if(livro.isPresent()) throw new GerenciamentoLivroException("Usuario já possui um livro com esse titulo cadastrado");
    		
    		Livro novoLivro = new Livro(titulo, usuario.get());
    		
    		return livroRepo.save(novoLivro);
    };
    public Livro consultarLivro(String titulo, UUID idUsuario) throws Exception{
    	if(idUsuario == null || titulo == null) throw new NullPointerException("Não é permitivo consultar livros com atributos nulos");

    	var usuario = userRepo.findById(idUsuario);
    	if(usuario.isEmpty()) throw new GerenciamentoUsuariosException("Usuario não foi cadastrado");
    	
		var  livro = livroRepo.findByTituloAndUsuario_id(titulo, idUsuario);
		if(livro.isEmpty()) throw new GerenciamentoLivroException("Usuario não possui um livro com esse titulo");
		
		
        return livro.get();   
       };
    public Boolean excluirLivro(Long id, UUID idUsuario, String senha){
    	if(idUsuario == null || senha == null || id == null) return false;
    	var usuario = userRepo.findById(idUsuario);
    	if(usuario.isEmpty()) return false;
    	
		var  livro = livroRepo.findById(id);
		if(livro.isEmpty()) return false;
		
		livroRepo.delete(livro.get());
        return true;   
    };
    public List<Livro> listarLivros(UUID idUsuario) throws Exception{
    	
    	var usuario = userRepo.findById(idUsuario);
    	if(usuario.isEmpty()) throw new GerenciamentoUsuariosException("Usuario não foi cadastrado");
    	
    	var livros = livroRepo.findAllByUsuario_Id(idUsuario);
    	
    	if(livros.isEmpty())throw new GerenciamentoLivroException("Usuario não possui um livros cadastrados");
    	if(livros.get().isEmpty())throw new GerenciamentoLivroException("Usuario não possui um livros cadastrados, lista de livros vazia");

		

        return livros.get();   
    };

}
