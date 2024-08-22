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
					.orElseThrow(() -> new EntidadeNaoEncontrada("A Usuario não foi encontrado"));

			return new UsuarioDTO(resposta);

		} catch (InvalidDataAccessApiUsageException e) {
			throw new PermissaoNegadaException("Operação de consulta não foi permitida");
		}
	};

	@Transactional(readOnly = true)
	public UsuarioDTO logar(String nome, String senha) {

		try {
			var user = userRepo.findByNome(nome);

			// Lembrar de descriptografar a senha antes da comparação.

			Usuario usuario = user
					.orElseThrow(() -> new EntidadeNaoEncontrada("Usuario com tau nome não foi encontrado"));
			String senhaSalva = usuario.getSenha();

			if (senhaSalva.equals(senha)) {

				UsuarioDTO resposta = new UsuarioDTO(usuario);
				resposta.setSenha(null);
				resposta.setPalavraSegu(null);

				return resposta;

			} else
				throw new PermissaoNegadaException("Acesso negado");

		} catch (InvalidDataAccessApiUsageException e) {
			throw new PermissaoNegadaException("Acesso negado, o nome do usuarío se em encontra branco ou nulo");
		}
	};

	@Transactional
	public UsuarioDTO trocarSenha(Long id, String novasenha, String palavra) {

		try {
			var user = userRepo.findById(id);

			Usuario usuario = user.orElseThrow(() -> new EntidadeNaoEncontrada("Usuario não encontrado"));

			if (usuario.getPalavraSegu().equals(palavra)) {

				// lembrar de criotografar a senha posteriormente;
				usuario.setSenha(novasenha);
				UsuarioDTO resposta = new UsuarioDTO(userRepo.save(usuario));
				return resposta;

			} else
				throw new PermissaoNegadaException("Acesso negado, troca de senha não permitida");

		} catch (DataIntegrityViolationException e) {
			throw new PermissaoNegadaException(e.getMessage());
		} catch (InvalidDataAccessApiUsageException e) {
			throw new PermissaoNegadaException("não foi possivel trocara senha" + e.getMessage());
		}
	};
	
	@Transactional
	public UsuarioDTO recuperarSenha(String nome, String novasenha, String palavra) throws Exception {
		try {
			var user = userRepo.findByNome(nome);

			Usuario usuario = user.orElseThrow(
					() -> new EntidadeNaoEncontrada("Usuario não encontrado"));

			UsuarioDTO resposta = trocarSenha(usuario.getId(), novasenha, palavra);
			return resposta;
		} catch (InvalidDataAccessApiUsageException e) {
			throw new PermissaoNegadaException("não foi possivel trocara senha" + e.getMessage());
		}
	}
}
