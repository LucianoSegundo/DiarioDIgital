package com.LSoftwareLTDA.diarioDigital.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.LSoftwareLTDA.diarioDigital.controller.dto.livro.response.LivroResponse;
import com.LSoftwareLTDA.diarioDigital.entidades.Livro;
import com.LSoftwareLTDA.diarioDigital.entidades.Usuario;
import com.LSoftwareLTDA.diarioDigital.repositorios.LivroRepositorio;
import com.LSoftwareLTDA.diarioDigital.repositorios.UsuarioRepositorio;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.CadastroNegadoException;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.EntidadeNaoEncontrada;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.PermissaoNegadaException;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.TokenInvalido;

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
	private BCryptPasswordEncoder codificador;

	public LivrosService(LivroRepositorio repositorio, UsuarioRepositorio userRepo, BCryptPasswordEncoder codificador) {
		this.livroRepo = repositorio;
		this.userRepo = userRepo;
		this.codificador = codificador;

	}

	@Transactional()
	public LivroResponse criarLivro(String titulo, Long idUsuario) {

		try {

			var entidade = userRepo.findById(idUsuario);

			Usuario usuario = entidade.orElseThrow(
					() -> new EntidadeNaoEncontrada("Usuario não foi encontrado, não é possivel criar um livro"));

			var livro = livroRepo.findByTituloAndUsuario_id(titulo, idUsuario);

			if (livro.isPresent())
				throw new CadastroNegadoException("Livro com este nome já foi cadastrado para este usuário");

			Livro novoLivro = new Livro(titulo, usuario);

			List<Livro> lista = usuario.getLivros();

			lista.add(novoLivro);
			usuario.setLivros(lista);
			userRepo.save(usuario);

			novoLivro = livroRepo.findByTituloAndUsuario_id(titulo, idUsuario)
					.orElseThrow(() -> new CadastroNegadoException());

			return new LivroResponse(novoLivro);

		} catch (DataIntegrityViolationException e) {
			throw new CadastroNegadoException("Livro com este nome já foi cadastrado para este usuário");
		} catch (ConstraintViolationException e) {
			throw new CadastroNegadoException(e.getMessage());
		} catch (InvalidDataAccessApiUsageException e) {
			throw new PermissaoNegadaException("Não foi possivel criar livro, identificador do usuário branco ou nulo");
		}

	};

	@Transactional(readOnly = true)
	public LivroResponse consultarLivro(Long idlivro, Long idUsuario) {

		var livro = livroRepo.findByIdAndUsuario_id(idlivro, idUsuario);
		Livro resposta = livro.orElseThrow(() -> new EntidadeNaoEncontrada("O Livro em questão não foi encontrado"));

		return new LivroResponse(resposta);

	};

	@Transactional()
	public Boolean excluirLivro(Long id, Long idUsuario, String senha) {

		try {
			var livro = livroRepo.findByIdAndUsuario_id(id, idUsuario).orElseThrow(
					() -> new EntidadeNaoEncontrada("Não foi possivel excluir o livro, livro não encontrado"));

			String senhaSalva = livro.getUsuario().getSenha();
			if ((senha !=null) && codificador.matches(senha, senhaSalva)) {

				livroRepo.delete(livro);
				return true;
			}
			throw new PermissaoNegadaException("senhas não coincidem, login negado");

		} catch (InvalidDataAccessApiUsageException e) {
			throw new PermissaoNegadaException("Não foi possivel excluir o livro, " + e.getMessage());
		}
	};

	@Transactional(readOnly = true)
	public Page<LivroResponse> listarLivros(Long idUsuario, Pageable pageable) {

		try {

			var livros = livroRepo.findAllByUsuario_Id(idUsuario, pageable);
			var resposta = livros.orElseThrow(() -> new EntidadeNaoEncontrada("Livros não foram encontrados"));

			return resposta.map(x -> new LivroResponse(x));

		} catch (InvalidDataAccessApiUsageException e) {
			throw new PermissaoNegadaException("permissão negada " + e.getMessage());
		}
	};

	@Transactional(readOnly = true)
	public Long extrairId(JwtAuthenticationToken token) {

		Long idToken = Long.parseLong(token.getName());

		if (userRepo.existsById(idToken)) {

			return idToken;
		} else
			throw new TokenInvalido("Usuario dono do token não foi encontrado");

	}
}
