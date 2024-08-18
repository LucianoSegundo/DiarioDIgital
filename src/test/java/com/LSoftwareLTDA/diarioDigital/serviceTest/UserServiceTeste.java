package com.LSoftwareLTDA.diarioDigital.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.LSoftwareLTDA.diarioDigital.controller.dto.UsuarioDTO;
import com.LSoftwareLTDA.diarioDigital.service.UserService;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.CadastroNegadoException;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.EntidadeNaoEncontrada;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.PermissaoNegadaException;


@ComponentScan(basePackages = "com.LSoftwareLTDA.diarioDigital.service")

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTeste {

	@Autowired
	UserService userServi;
	
// testes de cadastro
	
	@Test
	@DisplayName("Cadastrar usuário com sucesso")
	void CadastrarUserSucesso() throws Exception {
		
		UsuarioDTO dto = new UsuarioDTO("Noel Gallager", "12345689", "calopsita", 22);

		var usuario = userServi.cadastrarUsuario(dto);
		assertThat(usuario).describedAs("usuario retornou null").isNotNull();
		



	}

	@Test
	@DisplayName("Cadastrar usuário já cadastrado")
	void CadastrarUserCadastrado() throws Exception {

		UsuarioDTO dto = new UsuarioDTO("Alex Turner", "12345689", "calopsita", 22);

		var usuario = userServi.cadastrarUsuario(dto);
		
		assertThrows(CadastroNegadoException.class, () -> {
			userServi.cadastrarUsuario(dto);
		}, "Usuário que já estava cadastrado não foi barrada ao tentar se cadastrar");

	}
	
	@Test
	@DisplayName("Cadastrar usuario menor de idade")
	void CadastrarUserMenor() throws Exception {
		
		UsuarioDTO dto = new UsuarioDTO("David Grow", "12345689", "calopsita", 17);
		
		assertThrows(CadastroNegadoException.class, () -> {
			userServi.cadastrarUsuario(dto);
		}, "Cadastro que não deveria ser realizado, usuario menor de idade, foi realizado");

	}

// testes de login
	@Test
	@DisplayName("Logar com sucesso")
	void LoginSucesso() throws Exception {
		UsuarioDTO dto = new UsuarioDTO("Caio de Arruda Miranda", "12345689", "calopsita", 22);

		userServi.cadastrarUsuario(dto);
		
		var usuario = userServi.logar("Caio de Arruda Miranda", "12345689");
		assertThat(usuario).describedAs("cadastro retornou null").isNotNull();
		
	}

	@Test
	@DisplayName("Tentar logar sem ter conta cadastrada.")
	void LoginMalSucedido() throws Exception {

		assertThrows(EntidadeNaoEncontrada.class, () -> {
			userServi.logar("Caio de Arruda Miranda2", "12345689");
		}, "Exceção que deveria ser lançada ao barrar um login não foi lançada");

	}
	

// testes de recuperação de senha.
	
	@Test
	@DisplayName("Recuperar senha com sucesso.")
	void RecuperarSucesso() throws Exception {
		UsuarioDTO dto = new UsuarioDTO("Alex Turner2", "abacate", "calopcita", 22);

		userServi.cadastrarUsuario(dto);

		var usuario =userServi.recuperarSenha("Alex Turner2", "12345678", "calopcita");
		
		assertThat(usuario).describedAs("usuario retornol como nulo").isNotNull();
		assertThat(usuario.getSenha()).describedAs("senha não retornou igual a nova senha").isEqualTo("12345678");

	}
	
	@Test
	@DisplayName("Tentar recuperar sem conta registrada.")
	void RecuperarSemConta() throws Exception {

		assertThrows(EntidadeNaoEncontrada.class, () -> {
			userServi.recuperarSenha("Alex Turner3", "12345678", "calopcita");
		}, "Conta foi tratada como cadastrada, o teste falhou");

	}
	
	@Test
	@DisplayName("Tentar recuperar senha com palavra de segurança errada.")
	void RecuperarSemPalavra() throws Exception {

		UsuarioDTO dto = new UsuarioDTO("Alex Turner4", "12345678", "calopcita", 22);

		userServi.cadastrarUsuario(dto);

		assertThrows(PermissaoNegadaException.class, () -> {
			userServi.recuperarSenha("Alex Turner4", "12345678", "cacatua");
		}, " palavra de segurança foi tratada como correta, teste falhou");

	}
	
	
// excluir conta
	
	@Test
	@DisplayName("Excluir conta com sucesso")
	void ExcluirSucesso() throws Exception {
		
		UsuarioDTO usuario = new UsuarioDTO("Alex Turner5", "abacate", "calopcita", 22);

		
		 usuario  =userServi.cadastrarUsuario(usuario);
		Boolean resultado = userServi.excluirUsuario(usuario);		
		assertThat(resultado).describedAs("Usuario não foi excluido").isTrue();

	}
	
	@Test
	@DisplayName("Tentar excluir com senha errada")
	void FalharExcluir() throws Exception {
		
		UsuarioDTO usuario = new UsuarioDTO("Alex Turner6", "abacate", "calopcita", 22);

		
		var usuario2  =userServi.cadastrarUsuario(usuario);
		 usuario2.setSenha("111111111111111111111111111111111111111111");
		assertThrows(PermissaoNegadaException.class, ()->{
			userServi.excluirUsuario(usuario2);		
			
		});
		
	}
	@Test
	@DisplayName("Tentar excluir com id null")
	void ExcluirIDNull()  {
		
		UsuarioDTO usuario = new UsuarioDTO("Alex Turner8", "abacate", "calopcita", 22);

		
		var usuario2  =userServi.cadastrarUsuario(usuario);
	
		assertThrows(PermissaoNegadaException.class, ()->{
			userServi.excluirUsuario(usuario);		
			
		});
	}

	
}



