package com.LSoftwareLTDA.diarioDigital.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.LSoftwareLTDA.diarioDigital.controller.dto.LivroDTO;
import com.LSoftwareLTDA.diarioDigital.service.LivrosService;

@RestController
@RequestMapping(value = "/api/v1/livro")
public class LivroController {

	private LivrosService livroServi;

	public LivroController(LivrosService livroServi) {
		this.livroServi = livroServi;
	}

	@PostMapping(value = "/{userID}")
	public ResponseEntity<LivroDTO> criarLivro(@RequestBody LivroDTO dto, @PathVariable Long userID) {

		var resposta = livroServi.criarLivro(dto.getTitulo(), userID);

		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(resposta.getId())
				.toUri();

		return ResponseEntity.created(uri).body(resposta);
	}

	@GetMapping(value = "/listar/{userID}")
	public ResponseEntity<Page<LivroDTO>> listarLivros(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordarPor", defaultValue = "titulo") String ordarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem,
			@PathVariable Long userID) {

		PageRequest pagi = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordarPor);

		var resultado = livroServi.listarLivros(userID, pagi);

		return ResponseEntity.ok(resultado);
	}

	@GetMapping(value ="/{userID}/{id}")
	public ResponseEntity<LivroDTO>consultarLivro(@PathVariable Long userID, @PathVariable Long id){
		
		var resultado = livroServi.consultarLivro(id,userID);
		
		return ResponseEntity.ok(resultado);
	}
	
	@DeleteMapping(value = "/deletar/{userID}/{id}")
	public ResponseEntity<Void> deletarLivro(@RequestBody LivroDTO dto, @PathVariable Long userID, @PathVariable Long id) {

		livroServi.excluirLivro(id, userID, dto.getSenha());

		return ResponseEntity.noContent().build();
	}

}
