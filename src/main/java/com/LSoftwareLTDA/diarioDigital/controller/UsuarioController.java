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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.LSoftwareLTDA.diarioDigital.controller.dto.usuario.requisicoes.CadastroRequest;
import com.LSoftwareLTDA.diarioDigital.controller.dto.usuario.requisicoes.TrocaSenhaRequest;
import com.LSoftwareLTDA.diarioDigital.controller.dto.usuario.requisicoes.loginRequest;
import com.LSoftwareLTDA.diarioDigital.controller.dto.usuario.resposta.UsuarioResponse;
import com.LSoftwareLTDA.diarioDigital.controller.dto.usuario.resposta.loginResponse;
import com.LSoftwareLTDA.diarioDigital.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(value = "/api/v1/usuario")
public class UsuarioController {

	private UserService userServi;

	public UsuarioController(UserService userServi) {
		this.userServi = userServi;
	}

	@Operation(summary = "Cadastra um usuário", description = "recebe o nome, senha, idade e palavra de segurança de um usuário para cadastra-lo")
	@ApiResponse(responseCode = "200", description = "Usuario cadastrado com sucesso")
	@ApiResponse(responseCode = "400", description = "Cadastro negado devido a falta de parametros essenciais")
	@ApiResponse(responseCode = "406", description = "Cadastro negado devido a existencia do mesmo usuário no banco de dados")
	@PostMapping(value = "/cadastro")
	public ResponseEntity<UsuarioResponse> cadastrarUsuario(@RequestBody CadastroRequest request) {

		UsuarioResponse resposta = userServi.cadastrarUsuario(request);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(resposta.id()).toUri();

		return ResponseEntity.created(uri).body(resposta);
	}

	@Operation(summary = "Logar no sistema", description = "Receber login e senha para obter acesso ao sistema")
	@ApiResponse(responseCode = "200", description = "Login permitido com sucesso")
	@ApiResponse(responseCode = "400", description = "Permissão de login negada devido a senha invalida")
	@ApiResponse(responseCode = "404", description = "Permissão de login negada devido a nome de usuário invalido")
	@PostMapping(value = "/login")
	public ResponseEntity<loginResponse> logaeUsuario(@RequestBody loginRequest request) {

		loginResponse resposta = userServi.logar(request);

		return ResponseEntity.ok(resposta);
	}

	@Operation(summary = "Obter lista de usuários paginada", description = "obter lista de usuários paginada")
	@ApiResponse(responseCode = "200", description = "Lista adquirida com sucesso")
	@GetMapping(value = "/listar")
	public ResponseEntity<Page<UsuarioResponse>> listarUsuarios(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhas", defaultValue = "10") Integer linhas,
			@RequestParam(value = "ordenarPor", defaultValue = "nome") String ordenarPor,
			@RequestParam(value = "ordem", defaultValue = "ASC") String ordem, JwtAuthenticationToken token) {

		PageRequest pagi = PageRequest.of(pagina, linhas, Direction.valueOf(ordem), ordenarPor);

		Page<UsuarioResponse> resposta = userServi.consultarUsuarios(pagi);

		return ResponseEntity.ok(resposta);
	}

	@Operation(summary = "Obter um usuário", description = "obter um usuário")
	@ApiResponse(responseCode = "200", description = "Usuario retornado com sucesso")
	@ApiResponse(responseCode = "400", description = "operação de consulta não foi permitid")
	@ApiResponse(responseCode = "404", description = "Usuario não encontrado")
	@GetMapping(value = "/{id}")
	public ResponseEntity<UsuarioResponse> consultarUsuario(@PathVariable Long id, JwtAuthenticationToken token) {

		var resultado = userServi.consultar(id);

		return ResponseEntity.ok(resultado);
	}

	@Operation(summary = "Trocar senha do usuário", description = "Receber palavra de segurança e nova senha para trocar a senha do usuário")
	@ApiResponse(responseCode = "200", description = "Senha trocada com sucesso")
	@ApiResponse(responseCode = "400", description = "não foi possivel trocara senha")
	@ApiResponse(responseCode = "404", description = "Usuario não encontrado")
	@PutMapping(value = "/trocarSenha")
	public ResponseEntity<UsuarioResponse> trocarSenha(@RequestBody TrocaSenhaRequest request,
			JwtAuthenticationToken token) {

		UsuarioResponse resposta = userServi.trocarSenha(
				userServi.extrairId(token),
				request.novaSenha(),
				request.palavraSeguranca());

		return ResponseEntity.ok(resposta);
	}

	@Operation(summary = "Excluir Usuario", description = "Receber senha do usuário para realizar a exclusão")
	@ApiResponse(responseCode = "200", description = "Usuario excluido com sucesso")
	@ApiResponse(responseCode = "400", description = "Não foi permitido a exclussão deste recurso")
	@ApiResponse(responseCode = "404", description = "Usuario não encontrado")
	@DeleteMapping(value = "/excluir")
	public ResponseEntity<Void> deletarUsuario(@RequestBody String senha, JwtAuthenticationToken token) {

		
		 

		userServi.excluirUsuario(userServi.extrairId(token), senha);

		return ResponseEntity.noContent().build();
	}

}
