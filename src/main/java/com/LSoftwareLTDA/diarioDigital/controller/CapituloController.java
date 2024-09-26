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

import com.LSoftwareLTDA.diarioDigital.controller.dto.capitulos.CapituloRequest;
import com.LSoftwareLTDA.diarioDigital.controller.dto.capitulos.CapituloResponse;
import com.LSoftwareLTDA.diarioDigital.service.CapituloService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@CrossOrigin(origins = "${li.origem}")
@RequestMapping(value = "/api/v1/capitulo")
public class CapituloController {

	private CapituloService capServi;

	public CapituloController(CapituloService capServi) {
		this.capServi = capServi;
	}

	@Operation(summary = "Criar capitulo", description = "criar um capitulo recebendo titulo, conteudo")
	@ApiResponse(responseCode = "200", description = "Capitulo criado com sucesso")
	@ApiResponse(responseCode = "404", description = "Usuario ou livro não encontrados") // entidade não encontrada
	@ApiResponse(responseCode = "400", description = "Criação negada devido a atributos nulos")// permisão negada
	@ApiResponse(responseCode = "406", description = "Cadastro negado por já existir um item com esse titulo") //cadastro negado
	@PostMapping(value = "/{livroID}")
	public ResponseEntity<CapituloResponse> criarCapitulo(@RequestBody CapituloRequest request, @PathVariable Long livroID,JwtAuthenticationToken token) {

		CapituloResponse resposta = capServi.criarCapitulo(request, livroID, capServi.extrairId(token));
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(resposta.id())
				.toUri();

		return ResponseEntity.created(uri).body(resposta);
	}

	@Operation(summary = "Consultar lista de capitulos", description = "Consultar a lista de capitulos de um livro")
	@ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
	@GetMapping(value = "/listar/{livroID}")
	public ResponseEntity<Page<CapituloResponse>> listarCapitulo(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordarPor", defaultValue = "titulo") String ordarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem, 
			@PathVariable Long livroID,
			JwtAuthenticationToken token) {

		PageRequest pagi = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordarPor);

		var resultado = capServi.listarCapitulo(livroID, capServi.extrairId(token), pagi);

		return ResponseEntity.ok(resultado);
	}

	@Operation(summary = "consultar capitulo", description = "consultar um capitulo")
	@ApiResponse(responseCode = "200", description = "Capitulo consultado com sucesso")
	@ApiResponse(responseCode = "404", description = "Usuario ou livro não encontrados") // entidade não encontrada
	@ApiResponse(responseCode = "400", description = "Consulta negada devido a atributos nulos")// permisão negada
	@GetMapping(value = "/{livroID}/{id}")
	public ResponseEntity<CapituloResponse> consultarCapitulo(@PathVariable Long livroID, @PathVariable Long id,JwtAuthenticationToken token) {

		var resultado = capServi.consultarCapitulo(livroID, id);

		return ResponseEntity.ok(resultado);
	}

	@Operation(summary = "excluir capitulo", description = "excluir um capitulo ")
	@ApiResponse(responseCode = "200", description = "Capitulo excluido com sucesso")
	@ApiResponse(responseCode = "404", description = "Usuario ou livro não encontrados") // entidade não encontrada
	@DeleteMapping(value = "/deletar/{livroID}/{id}")
	public ResponseEntity<Void> deletarCapitulo(@PathVariable Long livroID, @PathVariable Long id, JwtAuthenticationToken token) {

		capServi.excluirCapitulo(id, livroID, capServi.extrairId(token));

		return ResponseEntity.noContent().build();
	}

}
