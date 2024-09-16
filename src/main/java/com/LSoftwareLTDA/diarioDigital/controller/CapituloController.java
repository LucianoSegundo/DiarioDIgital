package com.LSoftwareLTDA.diarioDigital.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.LSoftwareLTDA.diarioDigital.controller.dto.CapituloDTO;
import com.LSoftwareLTDA.diarioDigital.service.CapituloService;

@RestController
@RequestMapping(value = "/api/v1/capitulo")
public class CapituloController {

	private CapituloService capServi;

	public CapituloController(CapituloService capServi) {
		this.capServi = capServi;
	}

	@PostMapping(value = "/{livroID}")
	public ResponseEntity<CapituloDTO> criarCapitulo(@RequestBody CapituloDTO dto, @PathVariable Long livroID,JwtAuthenticationToken token) {

		dto = capServi.setarParametros(token, dto, livroID);

		var resultado = capServi.criarCapitulo(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(resultado.getId())
				.toUri();

		return ResponseEntity.created(uri).body(dto);
	}

	@GetMapping(value = "/listar/{livroID}")
	public ResponseEntity<Page<CapituloDTO>> listarCapitulo(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordarPor", defaultValue = "titulo") String ordarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem, 
			@PathVariable Long livroID,
			JwtAuthenticationToken token) {

		CapituloDTO dto = capServi.setarParametros(token, livroID);

		PageRequest pagi = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordarPor);

		var resultado = capServi.listarCapitulo(dto, pagi);

		return ResponseEntity.ok(resultado);
	}

	@GetMapping(value = "/{livroID}/{id}")
	public ResponseEntity<CapituloDTO> consultarCapitulo(@PathVariable Long livroID, @PathVariable Long id,JwtAuthenticationToken token) {
		CapituloDTO dto = new CapituloDTO();

		dto = capServi.setarParametros(token, dto, id, livroID);

		var resultado = capServi.consultarCapitulo(dto);

		return ResponseEntity.ok(resultado);
	}

	@DeleteMapping(value = "/deletar/{livroID}/{id}")
	public ResponseEntity<Void> deletarCapitulo(@PathVariable Long livroID, @PathVariable Long id, JwtAuthenticationToken token) {
		CapituloDTO dto = new CapituloDTO();

		dto = capServi.setarParametros(token, dto, id, livroID);

		var resultado = capServi.consultarCapitulo(dto);

		capServi.excluirCapitulo(dto);

		return ResponseEntity.noContent().build();
	}

}
