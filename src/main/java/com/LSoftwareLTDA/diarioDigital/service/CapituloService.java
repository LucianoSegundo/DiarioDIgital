package com.LSoftwareLTDA.diarioDigital.service;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.LSoftwareLTDA.diarioDigital.controller.dto.CapituloDTO;
import com.LSoftwareLTDA.diarioDigital.entidades.Capitulo;
import com.LSoftwareLTDA.diarioDigital.entidades.Livro;
import com.LSoftwareLTDA.diarioDigital.repositorios.CapituloRepositorio;
import com.LSoftwareLTDA.diarioDigital.repositorios.LivroRepositorio;
import com.LSoftwareLTDA.diarioDigital.repositorios.UsuarioRepositorio;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.CadastroNegadoException;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.EntidadeNaoEncontrada;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.PermissaoNegadaException;

import jakarta.validation.ConstraintViolationException;

@Service
public class CapituloService {

	private LivroRepositorio livroRepp;
	private CapituloRepositorio capRepo;

	public CapituloService(UsuarioRepositorio repositorio, LivroRepositorio livroRepo, CapituloRepositorio capRepo) {
		this.livroRepp = livroRepo;
		this.capRepo = capRepo;

	}

	@Transactional
	public CapituloDTO criarCapitulo(CapituloDTO dto) {

		try {

			Livro livro = livroRepp.findByIdAndUsuario_id(dto.getIdLivro(), dto.getIdUsuario())
					.orElseThrow(() -> new EntidadeNaoEncontrada("Usuario ou livro não encontrados"));

			boolean teste = capRepo.existsByTituloAndLivro_id(dto.getTitulo(), dto.getIdLivro());
			if (teste)
				throw new CadastroNegadoException("Já existe um capitulo com este Titulo neste livro");

			Capitulo entidade = new Capitulo(dto, livro);
			entidade = capRepo.save(entidade);

			dto = new CapituloDTO(entidade);

			return dto;
		} catch (ConstraintViolationException e) {
			throw new CadastroNegadoException("cadastro negado: " + e.getMessage());
		} catch (InvalidDataAccessApiUsageException e) {
			throw new PermissaoNegadaException("Não foi possivel criar capitulo, Algum dos identificadores está nulo");
		}

	};

	@Transactional(readOnly = true)
	public CapituloDTO consultarCapitulo(CapituloDTO dto) {

		try {

			var capitulo = capRepo.findById(dto.getId())
					.orElseThrow(() -> new EntidadeNaoEncontrada("Entidade não encontrada"));

			return new CapituloDTO(capitulo);

		} catch (InvalidDataAccessApiUsageException e) {
			throw new PermissaoNegadaException(
					"Não foi possivel excluir capitulo, Algum dos identificadores está nulo");
		}
	}

	@Transactional
	public Boolean excluirCapitulo(CapituloDTO dto) {

		Livro livro = livroRepp.findByIdAndUsuario_id(dto.getIdLivro(), dto.getIdUsuario())
				.orElseThrow(() -> new EntidadeNaoEncontrada("Usuario ou livro não encontrados"));

		var obj = capRepo.findByIdAndLivro_id(dto.getId(), livro.getId());

		Capitulo entidade = obj.orElseThrow(
				() -> new EntidadeNaoEncontrada("Não foi possivel excluir capitulo, entidade não encontrada"));

		capRepo.delete(entidade);
		return true;
	};

	@Transactional(readOnly = true)
	public Page<CapituloDTO> listarCapitulo(CapituloDTO dto, Pageable pageable) {

		Livro livro = livroRepp.findByIdAndUsuario_id(dto.getIdLivro(), dto.getIdUsuario())
				.orElseThrow(() -> new EntidadeNaoEncontrada("Usuario ou livro não encontrados"));

		var capitulos = capRepo.findAllByLivro_id(livro.getId(), pageable);
		var resposta = capitulos.orElseThrow(() -> new EntidadeNaoEncontrada("Nenhum capitulo foi encontrado"));

		return resposta.map(x -> new CapituloDTO(x));

	};

}
