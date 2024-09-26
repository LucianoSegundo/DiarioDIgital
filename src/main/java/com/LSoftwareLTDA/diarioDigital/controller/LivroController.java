package com.LSoftwareLTDA.diarioDigital.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.LSoftwareLTDA.diarioDigital.controller.dto.livro.response.LivroResponse;
import com.LSoftwareLTDA.diarioDigital.service.LivrosService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@CrossOrigin(origins = "${origem}")
@RequestMapping(value = "/api/v1/livro")
public class LivroController {

	private LivrosService livroServi;

	public LivroController(LivrosService livroServi) {
		this.livroServi = livroServi;
	}

	@Operation(summary = "Criar um livro", description = "criar uma instancia do livro e associar a uma conta")
	@ApiResponse(responseCode = "200", description = "Livro criado com sucesso")
	@ApiResponse(responseCode = "404", description = "Usuario não encontrado, nao foi possivel criar o livro")
	@ApiResponse(responseCode = "406", description = "Criação não foi permitida devido a dado invalido")
	@ApiResponse(responseCode = "400", description = "Algum atributo está nulo")
	@PostMapping(value = "/criar")
	public ResponseEntity<LivroResponse> criarLivro(@RequestBody String titulo, JwtAuthenticationToken token) {

		LivroResponse resposta = livroServi.criarLivro(titulo, livroServi.extrairId(token));

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(resposta.id()).toUri();

		return ResponseEntity.created(uri).body(resposta);
	}

	@Operation(summary = "Listagem de livros", description = "Retorna uma lista de livros")
	@ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
	@ApiResponse(responseCode = "404", description = "Livros não foram encontrados")
	@ApiResponse(responseCode = "400", description = "Algum atributo está nulo")
	@GetMapping(value = "/listar")
	public ResponseEntity<Page<LivroResponse>> listarLivros(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordarPor", defaultValue = "titulo") String ordarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem, JwtAuthenticationToken token) {

		PageRequest pagi = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordarPor);

		Page<LivroResponse> resultado = livroServi.listarLivros(livroServi.extrairId(token), pagi);

		return ResponseEntity.ok(resultado);
	}

	@Operation(summary = "Consultar um unico livro", description = "consultar os dados de um livro")
	@ApiResponse(responseCode = "200", description = "Livro retornado com sucesso")
	@ApiResponse(responseCode = "404", description = "Livro não foi encontrado")
	@GetMapping(value = "/{idLivro}")
	public ResponseEntity<LivroResponse> consultarLivro(@PathVariable Long idLivro, JwtAuthenticationToken token) {

		LivroResponse resultado = livroServi.consultarLivro(idLivro, livroServi.extrairId(token));

		return ResponseEntity.ok(resultado);
	}

	@Operation(summary = "Criar um livro", description = "criar uma instancia do livro e associar a uma conta")
	@ApiResponse(responseCode = "200", description = "Livro criado com sucesso")
	@ApiResponse(responseCode = "404", description = "livro não encontrado, nao foi possivel excluir o livro")
	@ApiResponse(responseCode = "400", description = "Exclusão do recurso não foi permitida")
	@DeleteMapping(value = "/deletar/{idLivro}")
	public ResponseEntity<Void> deletarLivro(@RequestBody String senha, @PathVariable Long idLivro,
			JwtAuthenticationToken token) {

		livroServi.excluirLivro(idLivro, livroServi.extrairId(token), senha);

		return ResponseEntity.noContent().build();
	}

}
