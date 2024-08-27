package com.LSoftwareLTDA.diarioDigital.controller.dto;

import java.util.ArrayList;
import java.util.List;

import com.LSoftwareLTDA.diarioDigital.entidades.Livro;
import com.LSoftwareLTDA.diarioDigital.entidades.Usuario;

public class UsuarioDTO {

    private Long id;
    private String nome;
    private String senha;
    private int idade;
    private String PalavraSegu;
    private String novaSenha;
    private Boolean ativo;
    
private List<Livro> livros;
    

	public UsuarioDTO() {
		// TODO Auto-generated constructor stub
	}
	  public UsuarioDTO(String nome, String senha, String palavra,int idade){
	        this.senha = senha;
	        this.nome = nome;
	        this.idade = idade;
	        this.PalavraSegu = palavra;
	        this.ativo = true;
	        this.livros = new ArrayList<>();

	    }
	public UsuarioDTO(Usuario entidade) {
		this.id= entidade.getId();
		this.nome = entidade.getNome();
		this.senha = entidade.getSenha();
		this.idade = entidade.getIdade();
		this.PalavraSegu = entidade.getPalavraSegu();
		this.ativo = entidade.getAtivo();
		this.livros = entidade.getLivros();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public int getIdade() {
		return idade;
	}
	public void setIdade(int idade) {
		this.idade = idade;
	}
	public String getPalavraSegu() {
		return PalavraSegu;
	}
	public void setPalavraSegu(String palavraSegu) {
		PalavraSegu = palavraSegu;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	public List<Livro> getLivros() {
		return livros;
	}
	public void setLivros(List<Livro> livros) {
		this.livros = livros;
	}
	public String getNovaSenha() {
		return novaSenha;
	}
	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

}
