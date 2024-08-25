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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.LSoftwareLTDA.diarioDigital.controller.dto.CapituloDTO;
import com.LSoftwareLTDA.diarioDigital.controller.dto.LivroDTO;
import com.LSoftwareLTDA.diarioDigital.service.CapituloService;

@RequestMapping(value = "/api/v1/capitulo")
public class CapituloController {

	private CapituloService capServi;

	public CapituloController(CapituloService capServi) {
		this.capServi = capServi;
	}

	@PostMapping(value = "/criar/{userID}")
	public ResponseEntity<CapituloDTO> criarCapitulo(CapituloDTO dto, @PathVariable Long userID) {

		dto.setIdUsuario(userID);

		var resultado = capServi.criarCapitulo(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(dto.getId()).toUri();

		return ResponseEntity.created(uri).body(dto);
	}

	@GetMapping(value = "/listar/{userID}")
	public ResponseEntity<Page<CapituloDTO>> listarCapitulo(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordarPor", defaultValue = "titulo") String ordarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem, @PathVariable Long userID,
			@RequestBody CapituloDTO dto) {

		dto.setIdUsuario(userID);
		
		PageRequest pagi = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordarPor);

		var resultado = capServi.listarCapitulo(dto, pagi);

		return ResponseEntity.ok(resultado);
	}

	@GetMapping(value ="/{userID}")
	public ResponseEntity<CapituloDTO>consultarCapitulo(@PathVariable Long userID, @RequestBody CapituloDTO dto){
		
		var resultado = capServi.consultarCapitulo(dto);
		
		return ResponseEntity.ok(resultado);
	}
	
	@DeleteMapping(value = "/deletar/{userID}")
	public ResponseEntity<Void> deletarCapitulo(@RequestBody CapituloDTO dto, @PathVariable Long userID) {

		dto.setIdUsuario(userID);
		capServi.excluirCapitulo(dto);

		return ResponseEntity.noContent().build();
	}

}
