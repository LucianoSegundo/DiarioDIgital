package com.LSoftwareLTDA.diarioDigital.entidades;

import java.util.ArrayList;
import java.util.List;

import com.LSoftwareLTDA.diarioDigital.controller.dto.usuario.requisicoes.CadastroRequest;
import com.LSoftwareLTDA.diarioDigital.entidades.enumeracoes.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Campo nome não deve ser nulo")
    @Column(unique = true)
    private String nome;

    @NotBlank(message = "Campo senha não deve ser nulo")
    private String senha;
    
    @Min(value = 18,message = "Usuario precisa ter mais de 18 anos")
    private int idade;
    
    @NotBlank(message = "Campo palavra de segurança não deve ser nulo")
    private String PalavraSegu;

    private Boolean ativo;
    
    @OneToMany(mappedBy = "usuario",cascade = CascadeType.ALL)
    private List<Livro> livros;

	private Role role;
    
    public Usuario() {
    	
    } 
    
    public Usuario(String nome, String senha, String palavra,int idade){
        this.senha = senha;
        this.nome = nome;
        this.idade = idade;
        this.PalavraSegu = palavra;
        this.ativo = true;
        this.role = Role.USER;
        this.livros = new ArrayList<>();
    }
    
    public Usuario(CadastroRequest request) {
		
		this.nome = request.nome();
		this.senha = request.senha();
		this.idade = request.idade();
		this.PalavraSegu = request.palavraSegu();
	    this.ativo = true;
        this.role = Role.USER;
        this.livros = new ArrayList<>();
	}
	
    //get e seter adicionado pelo fato do eclipse não suportar lombok nativamente.
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
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

	public String getPalavraSegu() {
		return PalavraSegu;
	}

	public void setPalavraSegu(String palavraSegu) {
		PalavraSegu = palavraSegu;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
    

}


