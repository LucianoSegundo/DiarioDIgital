package com.LSoftwareLTDA.diarioDigital.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.LSoftwareLTDA.diarioDigital.controller.dto.LivroDTO;
import com.LSoftwareLTDA.diarioDigital.entidades.Livro;
import com.LSoftwareLTDA.diarioDigital.entidades.Usuario;
import com.LSoftwareLTDA.diarioDigital.repositorios.LivroRepositorio;
import com.LSoftwareLTDA.diarioDigital.repositorios.UsuarioRepositorio;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.CadastroNegadoException;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.EntidadeNaoEncontrada;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.PermissaoNegadaException;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
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
    public LivroDTO criarLivro(String titulo, Long idUsuario){
    		try {
    			
    			var entidade = userRepo.findById(idUsuario);
    			Usuario usuario = entidade.orElseThrow(() -> new EntidadeNaoEncontrada("Usuario não foi encontrado, não é possivel criar um livro"));
    			Livro novoLivro = new Livro(titulo, usuario);
    			novoLivro = livroRepo.save(novoLivro);
    			
    			return  new LivroDTO(novoLivro);
				
			} catch (IllegalArgumentException e) {
				throw new PermissaoNegadaException("Criação de livro não permitido, atributos não podem ser nulos");
			} catch(ConstraintViolationException e) {
				throw new CadastroNegadoException("Criação de livro negada, "+e.getMessage());
			}
    	
    };
    public LivroDTO consultarLivro(String titulo, Long idUsuario) throws Exception{

    	try {
    		
    		var livro = livroRepo.findByTituloAndUsuario_id(titulo, idUsuario);
			Livro resposta = livro.orElseThrow(() -> new EntidadeNaoEncontrada("O Livro em questão não foi encontrado"));
    	
			return new LivroDTO(resposta);  
    		
		} catch (IllegalArgumentException e) {
			throw new PermissaoNegadaException("Não foi possivel encontrar o livrp, parametros não podem ser nulos");
		}
    	
		
       };
    public Boolean excluirLivro(Long id, Long idUsuario, String senha){
    	
    	try {
    	var livro = livroRepo.findByIdAndUsuario_id(id, idUsuario);
    	livro.orElseThrow(() -> new EntidadeNaoEncontrada("Não foi possivel excluir o livro, livro não encontrado"));
		
		livroRepo.delete(livro.get());
        return true;   
			
		} catch (IllegalArgumentException e) {
			throw new PermissaoNegadaException("Não foi possivel excluir o livro, "+ e.getMessage());
		}
    };
    public Page<LivroDTO> listarLivros(UUID idUsuario, Pageable pageable) {
    	
    	try {
 
    	var livros = livroRepo.findAllByUsuario_Id(idUsuario, pageable);
    	var resposta = livros.orElseThrow(() -> new EntidadeNaoEncontrada("Livros não foram encontrados"));
    	
        return resposta.map(x -> new LivroDTO(x));
			
		} catch (IllegalArgumentException e) {
			throw new PermissaoNegadaException("permissão negada "+ e.getMessage());		
		}
    };

}
