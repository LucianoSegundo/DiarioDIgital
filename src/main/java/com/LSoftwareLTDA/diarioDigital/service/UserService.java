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

	public UserService(UsuarioRepositorio repositorio, RecuperacaoSenha recuperador) {
		this.userRepo = repositorio;
		this.recuperadorSenha = recuperador;

	}

	public Usuario cadastrar(String nome, String senha) {
		var user = userRepo.findByNome(nome);

		if (user.isPresent())
			return null;

		if (nome == null || senha == null)
			return null;
		//lembrar de criptografar a senha posteriormente.
		Usuario userNovo = new Usuario(nome, senha);

		return userRepo.save(userNovo);

	};

	public Boolean excluir(UUID id) {
		var user = userRepo.findById(id);

		if (!user.isPresent())
			return false;

		userRepo.delete(user.get());

		return true;
	};

	public Usuario procurar(UUID id) {
		var user = userRepo.findById(id);

		if (user.isPresent())
			return user.get();

		return null;
	};

	public Usuario logar(String nome, String senha) {
		var user = userRepo.findByNome(nome);
			
			String senhaSalva = user.get().getSenha();
		//Lembrar de descriptografar a senha antes da comparação.
		
		if (user.isPresent() && (senhaSalva.equals(senha) == true))
			return user.get();
			

		return null;
	};

	public Usuario trocarSenha(UUID id, String senha) {
		var user = userRepo.findById(id);

		if (user.isPresent()) {
			Usuario usuario =user.get();
			
			//lembrar de criotografar a senha posteriormente;
			usuario.setSenha(senha);
			return userRepo.save(usuario);
			
		}
		
		return null;
	};

}
