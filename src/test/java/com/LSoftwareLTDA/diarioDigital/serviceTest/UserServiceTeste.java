package com.LSoftwareLTDA.diarioDigital.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.LSoftwareLTDA.diarioDigital.excecoes.GerenciamentoUsuariosException;
import com.LSoftwareLTDA.diarioDigital.service.UserService;


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

		var usuario = userServi.cadastrarUsuario("Noel Gallager", "12345689", "calopsita", 22);

		assertThat(usuario).describedAs("usuario retornou null").isNotNull();
		assertThat(usuario.getId()).describedAs("Id do usuário não foi atribuido, retornou null").isNotNull();
		assertThat(usuario.getNome()).describedAs("nome do usuário não foi atribuido").isNotNull();
		assertThat(usuario.getSenha()).describedAs("senha do usuário não foi atribuida").isNotNull();




	}

	@Test
	@DisplayName("Cadastrar usuário já cadastrado")
	void CadastrarUserCadastrado() throws Exception {

		userServi.cadastrarUsuario("Alex Turner", "12345689", "calopsita", 22);

		assertThrows(GerenciamentoUsuariosException.class, () -> {
			userServi.cadastrarUsuario("Alex Turner", "12345689", "calopsita", 22);
		}, "Usuário que já estava cadastrado não foi barrada ao tentar se cadastrar");

	}
	
	@Test
	@DisplayName("Cadastrar usuario menor de idade")
	void CadastrarUserMenor() throws Exception {

		assertThrows(IllegalArgumentException.class, () -> {
			userServi.cadastrarUsuario("David Grow", "12345689", "calopsita", 17);
		}, "Cadastro que não deveria ser realizado, usuario menor de idade, foi realizado");

	}

// testes de login
	@Test
	@DisplayName("Logar com sucesso")
	void LoginSucesso() throws Exception {

		userServi.cadastrarUsuario("Caio de Arruda Miranda", "12345689", "calopsita", 22);
		
		var usuario = userServi.logar("Caio de Arruda Miranda", "12345689");
		assertThat(usuario).describedAs("cadastro retornou null").isNotNull();
		assertThat(usuario.getId()).describedAs("Id do usuário retornou null").isNotNull();
		assertThat(usuario.getNome()).describedAs("usuario com o nome diferente foi retornado").isEqualTo("Caio de Arruda Miranda");
		assertThat(usuario.getSenha()).describedAs("usuario com senha diferente foi retornado").isEqualTo("12345689");



	}

	@Test
	@DisplayName("Tentar logar sem ter conta cadastrada.")
	void LoginMalSucedido() throws Exception {

		assertThrows(GerenciamentoUsuariosException.class, () -> {
			userServi.logar("Caio de Arruda Miranda2", "12345689");
		}, "Exceção que deveria ser lançada ao barrar um login não foi lançada");

	}

// testes de recuperação de senha.
	
	@Test
	@DisplayName("Recuperar senha com sucesso.")
	void RecuperarSucesso() throws Exception {
		
		userServi.cadastrarUsuario("Alex Turner2", "abacate", "calopcita", 22);

		var usuario =userServi.recuperarSenha("Alex Turner2", "12345678", "calopcita");
		
		assertThat(usuario).describedAs("usuario retornol como nulo").isNotNull();
		assertThat(usuario.getSenha()).describedAs("senha não retornou igual a nova senha").isEqualTo("12345678");

	}
	
	@Test
	@DisplayName("Tentar recuperar sem conta registrada.")
	void RecuperarSemConta() throws Exception {

		assertThrows(GerenciamentoUsuariosException.class, () -> {
			userServi.recuperarSenha("Alex Turner3", "12345678", "calopcita");
		}, "Conta foi tratada como cadastrada, o teste falhou");

	}
	
	@Test
	@DisplayName("Tentar recuperar senha com palavra de segurança errada.")
	void RecuperarSemPalavra() throws Exception {

		var usuario = userServi.cadastrarUsuario("Alex Turner4", "12345678", "calopcita", 22);

		assertThrows(IllegalArgumentException.class, () -> {
			userServi.recuperarSenha("Alex Turner4", "12345678", "cacatua");
		}, " palavra de segurança foi tratada como correta, teste falhou");

	}
	
// excluir conta
	
	@Test
	@DisplayName("Excluir conta com sucesso")
	void ExcluirSucesso() throws Exception {
		
		var usuario = userServi.cadastrarUsuario("Alex Turner5", "abacate", "calopcita", 22);

		Boolean resultado = userServi.excluirUsuario(usuario.getId(), "abacate");		
		assertThat(resultado).describedAs("Usuario não foi excluido").isTrue();

	}
	
	@Test
	@DisplayName("Tentar excluir com senha errada")
	void FalharExcluir() throws Exception {
		
		var usuario = userServi.cadastrarUsuario("Alex Turner6", "abacate", "calopcita", 22);

		Boolean resultado = userServi.excluirUsuario(usuario.getId(), "shangrila");		
		assertThat(resultado).describedAs("Metodo retornou que o usuário foi excluido, algo deu errado").isFalse();

	}
	
}



