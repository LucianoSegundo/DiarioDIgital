package com.LSoftwareLTDA.diarioDigital.service;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.LSoftwareLTDA.diarioDigital.controller.dto.capitulos.CapituloRequest;
import com.LSoftwareLTDA.diarioDigital.controller.dto.capitulos.CapituloResponse;
import com.LSoftwareLTDA.diarioDigital.entidades.Capitulo;
import com.LSoftwareLTDA.diarioDigital.entidades.Livro;
import com.LSoftwareLTDA.diarioDigital.repositorios.CapituloRepositorio;
import com.LSoftwareLTDA.diarioDigital.repositorios.LivroRepositorio;
import com.LSoftwareLTDA.diarioDigital.repositorios.UsuarioRepositorio;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.CadastroNegadoException;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.EntidadeNaoEncontrada;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.PermissaoNegadaException;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.TokenInvalido;

import jakarta.validation.ConstraintViolationException;

@Service
public class CapituloService {

	private LivroRepositorio livroRepp;
	private CapituloRepositorio capRepo;
	private UsuarioRepositorio userRepo;

	public CapituloService(UsuarioRepositorio repositorio, LivroRepositorio livroRepo, CapituloRepositorio capRepo,
			UsuarioRepositorio userRepo) {
		this.livroRepp = livroRepo;
		this.capRepo = capRepo;
		this.userRepo = userRepo;

	}

	@Transactional
	public CapituloResponse criarCapitulo(CapituloRequest request, Long idLivro, Long idUsuario) {

		try {

			Livro livro = livroRepp.findByIdAndUsuario_id(idLivro, idUsuario)
					.orElseThrow(() -> new EntidadeNaoEncontrada("Usuario ou livro não encontrados"));

			boolean teste = capRepo.existsByTituloAndLivro_id(request.titulo(), idLivro);

			if (teste)
				throw new CadastroNegadoException("Já existe um capitulo com este Titulo neste livro");

			Capitulo entidade = new Capitulo(request, livro);
			entidade = capRepo.save(entidade);

			return new CapituloResponse(entidade);

		} catch (ConstraintViolationException e) {
			throw new CadastroNegadoException("cadastro negado");
		} catch (InvalidDataAccessApiUsageException e) {
			throw new PermissaoNegadaException("Não foi possivel criar capitulo, Algum dos identificadores está nulo");
		}

	};

	@Transactional(readOnly = true)
	public CapituloResponse consultarCapitulo(Long idLivro, Long id) {

		if(idLivro==null || id ==null)throw new PermissaoNegadaException(
				"Não foi possivel excluir capitulo, Algum dos identificadores está nulo");
		

			var capitulo = capRepo.findByIdAndLivro_id(id, idLivro)
					.orElseThrow(() -> new EntidadeNaoEncontrada("Entidade não encontrada"));

			return new CapituloResponse(capitulo);

		
	}

	@Transactional
	public Boolean excluirCapitulo(Long id, Long idLivro, Long usuarioID) {

		Livro livro = livroRepp.findByIdAndUsuario_id(idLivro, usuarioID)
				.orElseThrow(() -> new EntidadeNaoEncontrada("Usuario ou livro não encontrados"));

		var obj = capRepo.findByIdAndLivro_id(id, livro.getId());

		Capitulo entidade = obj.orElseThrow(
				() -> new EntidadeNaoEncontrada("Não foi possivel excluir capitulo, entidade não encontrada"));

		capRepo.delete(entidade);
		return true;
	};

	@Transactional(readOnly = true)
	public Page<CapituloResponse> listarCapitulo(Long idLivro, Long idUsuario, Pageable pageable) {

		Livro livro = livroRepp.findByIdAndUsuario_id(idLivro, idUsuario)
				.orElseThrow(() -> new EntidadeNaoEncontrada("Usuario ou livro não encontrados"));

		var capitulos = capRepo.findAllByLivro_id(livro.getId(), pageable);
		var resposta = capitulos.orElseThrow(() -> new EntidadeNaoEncontrada("Nenhum capitulo foi encontrado"));

		return resposta.map(x -> new CapituloResponse(x));

	};

	@Transactional(readOnly = true)
	public Long extrairId(JwtAuthenticationToken token) {

		Long idToken = Long.parseLong(token.getName());

		if (userRepo.existsById(idToken)) {

			return idToken;
		} else
			throw new TokenInvalido("Usuario dono do token não foi encontrado");

	}

	public LivroRepositorio getLivroRepp() {
		return livroRepp;
	}

	public void setLivroRepp(LivroRepositorio livroRepp) {
		this.livroRepp = livroRepp;
	}

	public CapituloRepositorio getCapRepo() {
		return capRepo;
	}

	public void setCapRepo(CapituloRepositorio capRepo) {
		this.capRepo = capRepo;
	}

	public UsuarioRepositorio getUserRepo() {
		return userRepo;
	}

	public void setUserRepo(UsuarioRepositorio userRepo) {
		this.userRepo = userRepo;
	}

}
