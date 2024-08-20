package com.LSoftwareLTDA.diarioDigital.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.LSoftwareLTDA.diarioDigital.controller.dto.UsuarioDTO;
import com.LSoftwareLTDA.diarioDigital.entidades.Usuario;
import com.LSoftwareLTDA.diarioDigital.interfaces.RecuperacaoSenha;
import com.LSoftwareLTDA.diarioDigital.repositorios.UsuarioRepositorio;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.CadastroNegadoException;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.EntidadeNaoEncontrada;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.PermissaoNegadaException;

import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Service
public class UserService {

	private UsuarioRepositorio userRepo;
	private RecuperacaoSenha recuperadorSenha;

	public UserService(UsuarioRepositorio repositorio, RecuperacaoSenha recuperador) {
		this.userRepo = repositorio;
		this.recuperadorSenha = recuperador;
		System.out.println("Bean UserService criado");
	}

	@Transactional
	public UsuarioDTO cadastrarUsuario(UsuarioDTO entidade) {

		try {
			// lembrar de criptografar a senha posteriormente.
			Usuario userNovo = new Usuario(entidade);
			entidade = new UsuarioDTO(userRepo.save(userNovo));
			return entidade;

		} catch (DataIntegrityViolationException e) {
			throw new CadastroNegadoException(e.getMessage());
		} catch (ConstraintViolationException e) {
			throw new CadastroNegadoException(e.getMessage());
		}

	};

	@Transactional
	public Boolean excluirUsuario(UsuarioDTO entidade) {
		try {
			var user = userRepo.findById(entidade.getId());

			Usuario usuario = user.orElseThrow(
					() -> new EntidadeNaoEncontrada("Usuario não foi encontrado, não é possivel excluilo"));

			if (user.get().getSenha().equals(entidade.getSenha())) {
				userRepo.delete(user.get());
				return true;
			} else
				throw new PermissaoNegadaException("Não foi permitido a exclussão deste recurso");

		} catch (DataIntegrityViolationException e) {
			throw new PermissaoNegadaException("Não foi possivel excluir usuario " + e.getMessage());
		} catch (InvalidDataAccessApiUsageException e) {
			throw new PermissaoNegadaException("Não foi possivel excluir usuario id do usuario não pode ser null");
		}
	};

	@Transactional(readOnly = true)
	public UsuarioDTO consultarUsuario(Long id) {

		try {
			var user = userRepo.findById(id);
			Usuario resposta = user
					.orElseThrow(() -> new EntidadeNaoEncontrada("A entidade em questão não foi encontrada"));

			return new UsuarioDTO(resposta);

		} catch (IllegalArgumentException e) {
			throw new PermissaoNegadaException("Não foi possivel encontro usuário");
		}
	};

	@Transactional(readOnly = true)
	public UsuarioDTO logar(String nome, String senha) {

		try {
			var user = userRepo.findByNome(nome);

			// Lembrar de descriptografar a senha antes da comparação.

			Usuario usuario = user
					.orElseThrow(() -> new EntidadeNaoEncontrada("Não foi encontrado usuario com este nome"));
			String senhaSalva = usuario.getSenha();

			if (senhaSalva.equals(senha)) {

				UsuarioDTO resposta = new UsuarioDTO(usuario);
				resposta.setSenha(null);
				resposta.setPalavraSegu(null);

				return resposta;

			} else
				throw new PermissaoNegadaException("Não foi possivel realizar o login");

		} catch (IllegalArgumentException e) {
			throw new PermissaoNegadaException("não foi possivel realizar login Id do usuario não deve ser null");
		}
	};

	@Transactional
	public UsuarioDTO trocarSenha(Long id, String novasenha, String palavra) {

		try {
			var user = userRepo.findById(id);

			Usuario usuario = user.orElseThrow(() -> new EntidadeNaoEncontrada("Usuario não foi encontrado"));

			if (usuario.getPalavraSegu().equals(palavra)) {

				// lembrar de criotografar a senha posteriormente;
				usuario.setSenha(novasenha);
				UsuarioDTO resposta = new UsuarioDTO(userRepo.save(usuario));
				return resposta;

			} else
				throw new PermissaoNegadaException("Palavra de segurança não condiz");

		} catch (ConstraintViolationException e) {
			throw new PermissaoNegadaException(e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new PermissaoNegadaException("não foi possivel trocara senha" + e.getMessage());
		}
	};

	public UsuarioDTO recuperarSenha(String nome, String novasenha, String palavra) throws Exception {
		try {
			var user = userRepo.findByNome(nome);

			Usuario usuario = user.orElseThrow(
					() -> new EntidadeNaoEncontrada("Usuario não foi encontrado, substituição de senha cancelada"));

			return trocarSenha(usuario.getId(), novasenha, palavra);
		} catch (ConstraintViolationException e) {
			throw new PermissaoNegadaException(e.getMessage());
		} catch (InvalidDataAccessApiUsageException e) {
			throw new PermissaoNegadaException("não foi possivel trocara senha" + e.getMessage());
		}
	}
}
