package com.LSoftwareLTDA.diarioDigital.service;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.LSoftwareLTDA.diarioDigital.entidades.Usuario;
import com.LSoftwareLTDA.diarioDigital.excecoes.CadastroUsuariosException;
import com.LSoftwareLTDA.diarioDigital.interfaces.RecuperacaoSenha;
import com.LSoftwareLTDA.diarioDigital.repositorios.UsuarioRepositorio;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Component
public class UserService {

	private UsuarioRepositorio userRepo;
	private RecuperacaoSenha recuperadorSenha;

	public UserService(UsuarioRepositorio repositorio, RecuperacaoSenha recuperador) {
		this.userRepo = repositorio;
		this.recuperadorSenha = recuperador;
		System.out.println("Bean UserService criado");
	}

	public Usuario cadastrar(String nome, String senha, String palavra, int idade) throws Exception {
		var user = userRepo.findByNome(nome);

		if (user.isPresent())
			throw new CadastroUsuariosException("Usuário já cadastrado");

		if (nome.equals(null) || senha.equals(null) || palavra.equals(null))
			throw new IllegalArgumentException("Cadastro não realizado, parametros não podem ser nulos");
		if (idade < 18)
			throw new IllegalArgumentException("Cadastro não realizado, usuário não pode ser menor de idade");

		// lembrar de criptografar a senha posteriormente.
		Usuario userNovo = new Usuario(nome, senha, palavra, idade);

		return userRepo.save(userNovo);

	};

	public Boolean excluir(UUID id,String senha) {
		var user = userRepo.findById(id);

		if (user.isPresent())
			if(user.get().getSenha().equals(senha)) {
			userRepo.delete(user.get());			
			return true;
		}

		return false;
	};

	public Usuario procurar(UUID id) {
		var user = userRepo.findById(id);

		if (user.isPresent())
			return user.get();

		return null;
	};

	public Usuario logar(String nome, String senha) throws CadastroUsuariosException {

		if (nome.equals(null))
			throw new IllegalArgumentException("Usuario não pode ser null");
		var user = userRepo.findByNome(nome);

		// Lembrar de descriptografar a senha antes da comparação.

		if (user.isPresent()) {

			String senhaSalva = user.get().getSenha();
			if (senhaSalva.equals(senha))
				return user.get();
		};

		System.out.println("develarçar erro : " + nome);

		throw new CadastroUsuariosException("Usurio não foi cadastrado");
	};

	public Usuario trocarSenha(UUID id, String novasenha, String palavra) throws Exception {
		var user = userRepo.findById(id);

		if (user.isPresent()) {
			
			Usuario usuario = user.get();

			if(usuario.getPalavraSegu().equals(palavra)) {
				
			// lembrar de criotografar a senha posteriormente;
			usuario.setSenha(novasenha);
			return userRepo.save(usuario);
			}
			else throw new IllegalArgumentException("Palavra de segurança não condiz");

		}

		return null;
	};
	
	public Usuario recuperarSenha(String nome, String novasenha, String palavra) throws Exception {
		if(nome.equals(null)|| novasenha.equals(null)|| palavra.equals(null)) throw new IllegalArgumentException("durante a recuperação de senha nenhum dos campos pode ser nulo.");
			
			var usuario = userRepo.findByNome(nome);
			
			if(usuario.isPresent()) {
				Usuario user = usuario.get();
				
				return trocarSenha(user.getId(), novasenha, palavra);
				
			}
			else throw new CadastroUsuariosException("Usuário não foi cadastrado, não é permitido exclui-lo");
	}
}
