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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.LSoftwareLTDA.diarioDigital.controller.dto.UsuarioDTO;
import com.LSoftwareLTDA.diarioDigital.service.UserService;

@RestController
@RequestMapping(value ="/api/v1/usuario")
public class UsuarioController {
	
	private UserService userServi;
	
	public UsuarioController(UserService userServi) {
		this.userServi = userServi;
	}
	
	@PostMapping(value ="/cadastro")
	public ResponseEntity<UsuarioDTO> cadastrarUsuario(@RequestBody UsuarioDTO dto){
		
		dto = userServi.cadastrarUsuario(dto);
		
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(dto.getId())
				.toUri();
		
		return ResponseEntity.created(uri).body(dto);
	}
	
	@GetMapping(value ="/listar")
	public ResponseEntity<Page<UsuarioDTO>> listarUsuarios(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordenarPor", defaultValue = "nome") String ordenarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem
			){
		
		PageRequest pagi =  PageRequest.of(pagina, linhas, Direction.valueOf(ordem),ordenarPor);
		
		var resposta = userServi.consultarUsuarios(pagi);
		
		return ResponseEntity.ok(resposta);
	}
	
	@GetMapping(value ="/{id}")
	public ResponseEntity<UsuarioDTO>consultarUsuario(@PathVariable Long id){
		
		var resultado = userServi.consultar(id);
		
		return ResponseEntity.ok(resultado);
	}
	
	@PutMapping(value ="/trocarSenha/{id}")
	public ResponseEntity<UsuarioDTO>  trocarSenha(@PathVariable Long id, @RequestBody UsuarioDTO dto){
		
		var resposta = userServi.trocarSenha(id, dto.getNovaSenha(), dto.getPalavraSegu());
		
		return ResponseEntity.ok(resposta);
	}
	
	@DeleteMapping(value ="/excluir")
	public ResponseEntity<Void> deletarUsuario(@RequestBody UsuarioDTO dto){
		userServi.excluirUsuario(dto);
		
		return ResponseEntity.noContent().build();
	}

}
