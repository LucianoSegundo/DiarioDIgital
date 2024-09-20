package com.LSoftwareLTDA.diarioDigital.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.LSoftwareLTDA.diarioDigital.controller.dto.usuario.requisicoes.CadastroRequest;
import com.LSoftwareLTDA.diarioDigital.controller.dto.usuario.requisicoes.loginRequest;
import com.LSoftwareLTDA.diarioDigital.controller.dto.usuario.resposta.UsuarioResponse;
import com.LSoftwareLTDA.diarioDigital.service.UserService;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.CadastroNegadoException;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.EntidadeNaoEncontrada;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.PermissaoNegadaException;


@ComponentScan(basePackages = "com.LSoftwareLTDA.diarioDigital.service")

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTeste {

	@Autowired
	private UserService userServi;
	@Autowired
	private BCryptPasswordEncoder codificadorSenha;

	

	
// testes de cadastro
	
	@Test
	@DisplayName("Cadastrar usuário com sucesso")
	void CadastrarUserSucesso() throws Exception {
		
		CadastroRequest request = new CadastroRequest("Noel Gallager", "1234", "calopsita", 22);
		var usuario = userServi.cadastrarUsuario(request);
		
		assertThat(usuario).describedAs("usuario retornou null").isNotNull();
		excluirUsuario(usuario.id());

	}

	@Test
	@DisplayName("Cadastrar usuário já cadastrado")
	void CadastrarUserCadastrado() throws Exception {

		CadastroRequest request = new CadastroRequest("Alex Turner", "1234", "calopsita", 22);
		var usuario = userServi.cadastrarUsuario(request);
		
		assertThrows(CadastroNegadoException.class, () -> {
			userServi.cadastrarUsuario(request);
		}, "Usuário que já estava cadastrado não foi barrada ao tentar se cadastrar");
		
		excluirUsuario(usuario.id());

	}
	
	@Test
	@DisplayName("Cadastrar usuario menor de idade")
	void CadastrarUserMenor() throws Exception {
		
		CadastroRequest request = new CadastroRequest("David Grow", "1234", "calopsita", 17);
		
		assertThrows(PermissaoNegadaException.class, () -> {
			userServi.cadastrarUsuario(request);
		}, "Cadastro que não deveria ser realizado, usuario menor de idade, foi realizado");

	}

// testes de login
	@Test
	@DisplayName("Logar com sucesso")
	void LoginSucesso() throws Exception {
		
		CadastroRequest request = new CadastroRequest("Caio de Arruda Miranda", "1234", "calopsita", 22);
		UsuarioResponse usuario = userServi.cadastrarUsuario(request);
		
		var usuarioLogin = userServi.logar(new loginRequest("Caio de Arruda Miranda", "1234"));
		
		assertThat(usuarioLogin).describedAs("cadastro retornou null").isNotNull();
		excluirUsuario(usuario.id());

	}

	@Test
	@DisplayName("Tentar logar sem ter conta cadastrada.")
	void LoginMalSucedido() throws Exception {

		assertThrows(EntidadeNaoEncontrada.class, () -> {
			 userServi.logar(new loginRequest("Caio de Arruda Miranda2", "1234"));
		}, "Exceção que deveria ser lançada ao barrar um login não foi lançada");

	}
	

// testes de recuperação de senha.
	
	@Test
	@DisplayName("Recuperar senha com sucesso.")
	void RecuperarSucesso() throws Exception {
		
		CadastroRequest request = new CadastroRequest("Alex Turner2", "abacate", "calopcita", 22);
		userServi.cadastrarUsuario(request);
		var usuario = userServi.recuperarSenha("Alex Turner2", "1234", "calopcita");
		
		assertThat(usuario).describedAs("usuario retornol como nulo").isNotNull();
		
		excluirUsuario(usuario.id());

	}
	
	@Test
	@DisplayName("Tentar recuperar sem conta registrada.")
	void RecuperarSemConta() throws Exception {

		assertThrows(EntidadeNaoEncontrada.class, () -> {
			userServi.recuperarSenha("Alex Turner3", "1234", "calopcita");
		}, "Conta foi tratada como cadastrada, o teste falhou");

	}
	
	@Test
	@DisplayName("Tentar recuperar senha com palavra de segurança errada.")
	void RecuperarSemPalavra() throws Exception {

		CadastroRequest request = new CadastroRequest("Alex Turner4", "1234", "calopcita", 22);

		UsuarioResponse usuario = userServi.cadastrarUsuario(request);

		assertThrows(PermissaoNegadaException.class, () -> {
			userServi.recuperarSenha("Alex Turner4", "1234", "cacatua");
		}, " palavra de segurança foi tratada como correta, teste falhou");
		
		excluirUsuario(usuario.id());

	}
	
// excluir conta
	
	@Test
	@DisplayName("Excluir conta com sucesso")
	void ExcluirSucesso() throws Exception {
		
		CadastroRequest request = new CadastroRequest("Alex Turner5", "abacate", "calopcita", 22);

		
		UsuarioResponse usuario  = userServi.cadastrarUsuario(request);
		Boolean resultado = userServi.excluirUsuario(usuario.id(), request.senha());		
		assertThat(resultado).describedAs("Usuario não foi excluido").isTrue();

	}
	
	@Test
	@DisplayName("Tentar excluir com senha errada")
	void FalharExcluir() throws Exception {
		
		CadastroRequest request = new CadastroRequest("Alex Turner6", "1234", "calopcita", 22);
		UsuarioResponse usuario  =userServi.cadastrarUsuario(request);
		
		assertThrows(PermissaoNegadaException.class, ()->{
			userServi.excluirUsuario(usuario.id(),"1111" );		
			
		});
		
		excluirUsuario(usuario.id());

		
	}
	
	@Test
	@DisplayName("Tentar excluir com id null")
	void ExcluirIDNull()  {
		
	
		assertThrows(PermissaoNegadaException.class, ()->{
			userServi.excluirUsuario(null, "aaaaa" );		
			
		});
	}
	
	private void excluirUsuario(Long id) {

		 userServi.excluirUsuario(id,"1234" );
	}
	

	
}



