package com.LSoftwareLTDA.diarioDigital.entidades;

import java.util.ArrayList;
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

    @NotBlank(message = "Campo nome não deve ser nulo")
    private String nome;

    @NotBlank(message = "Campo senha não deve ser nulo")
    private String senha;
 
    
    private int idade;
    
    @NotBlank(message = "Campo palavra de segurança não deve ser nulo")
    private String PalavraSegu;

    private Boolean ativo;
    

    @OneToMany
    private List<Livro> livros;

    public Usuario() {} 
    
    public Usuario(String nome, String senha, String palavra,int idade){
        this.livros = new ArrayList<Livro>();
        this.senha = senha;
        this.nome = nome;
        this.idade = idade;
        this.PalavraSegu = palavra;
        this.ativo = true;
    }
    //get e seter adicionado pelo fato do eclipse não suportar lombok nativamente.
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
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
	
    

}
