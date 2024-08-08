package com.LSoftwareLTDA.diarioDigital.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.isNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.LSoftwareLTDA.diarioDigital.excecoes.CadastroUsuariosException;
import com.LSoftwareLTDA.diarioDigital.service.UserService;

@ComponentScan(basePackages = "com.LSoftwareLTDA.diarioDigital.service")

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTeste {

	@Autowired
	UserService userServi;
	
// testes de cadastro
	
	@Test
	@DisplayName("Cadastrar: Ao cadastrar deve retornar o usuário, isso se o nome do"
			+ "usuário não foi cadastrado e a senha é valida")
	void CadastrarUserSucesso() throws Exception {

		var usuario = userServi.cadastrar("Noel Gallager", "12345689", "calopsita", 22);

		assertThat(usuario).describedAs("usuario retornou null").isNotNull();
		assertThat(usuario.getId()).describedAs("Id do usuário não foi atribuido, retornou null").isNotNull();
		assertThat(usuario.getNome()).describedAs("nome do usuário não foi atribuido").isNotNull();
		assertThat(usuario.getSenha()).describedAs("senha do usuário não foi atribuida").isNotNull();




	}

	@Test
	@DisplayName("Cadastrar: Ao repetir cadastro deve larçar uma excessão")
	void CadastrarUserCadastrado() throws Exception {

		userServi.cadastrar("Alex Turner", "12345689", "calopsita", 22);

		assertThrows(CadastroUsuariosException.class, () -> {
			userServi.cadastrar("Alex Turner", "12345689", "calopsita", 22);
		}, "Usuário que já estava cadastrado não foi barrada ao tentar se cadastrar");

	}
	
	@Test
	@DisplayName("Cadastrar: Ao tentar cadastrar um menor deve larçar uma excessão")
	void CadastrarUserMenor() throws Exception {

		assertThrows(IllegalArgumentException.class, () -> {
			userServi.cadastrar("David Grow", "12345689", "calopsita", 17);
		}, "Cadastro que não deveria ser realizado, usuario menor de idade, foi realizado");

	}

// testes de login
	@Test
	@DisplayName("Logar: Ao logar Deve retornar o objeto do usuário")
	void LoginSucesso() throws Exception {

		userServi.cadastrar("Caio de Arruda Miranda", "12345689", "calopsita", 22);
		
		var usuario = userServi.logar("Caio de Arruda Miranda", "12345689");
		assertThat(usuario).describedAs("cadastro retornou null").isNotNull();
		assertThat(usuario.getId()).describedAs("Id do usuário retornou null").isNotNull();
		assertThat(usuario.getNome()).describedAs("usuario com o nome diferente foi retornado").isEqualTo("Caio de Arruda Miranda");
		assertThat(usuario.getSenha()).describedAs("usuario com senha diferente foi retornado").isEqualTo("12345689");



	}

	@Test
	@DisplayName("Logar: Deve retornar exceção caso tente fazer login de usuário sem cadastro.")
	void LoginMalSucedido() throws Exception {

		assertThrows(CadastroUsuariosException.class, () -> {
			userServi.logar("Caio de Arruda Miranda2", "12345689");
		}, "Exceção que deveria ser lançada ao barrar um login não foi lançada");

	}

// testes de recuperação de senha.
	
	@Test
	@DisplayName("Recuperar: Ao recuperar senha deve retornar o objeto do usuário")
	void RecuperarSucesso() throws Exception {
		
		userServi.cadastrar("Alex Turner2", "abacate", "calopcita", 22);

		var usuario =userServi.recuperarSenha("Alex Turner2", "12345678", "calopcita");
		
		assertThat(usuario).describedAs("usuario retornol como nulo").isNotNull();
		assertThat(usuario.getSenha()).describedAs("senha não retornou igual a nova senha").isEqualTo("12345678");

	}
	
	@Test
	@DisplayName("Recuperar: Ao recuperar sem conta cadastrada deve retornar exceção.")
	void RecuperarSemConta() throws Exception {

		assertThrows(CadastroUsuariosException.class, () -> {
			userServi.recuperarSenha("Alex Turner3", "12345678", "calopcita");
		}, "Conta foi tratada como cadastrada, o teste falhou");

	}
	
	@Test
	@DisplayName("Recuperar: Ao recuper com palavra de segurança errada deve retornar exceção.")
	void RecuperarSemPalavra() throws Exception {

		var usuario = userServi.cadastrar("Alex Turner4", "12345678", "calopcita", 22);

		assertThrows(IllegalArgumentException.class, () -> {
			userServi.recuperarSenha("Alex Turner4", "12345678", "cacatua");
		}, " palavra de segurança foi tratada como correta, teste falhou");

	}
	
// excluir conta
	
	@Test
	@DisplayName("Excluir: Ao excluir com sucesse deve retornar true")
	void ExcluirSucesso() throws Exception {
		
		var usuario = userServi.cadastrar("Alex Turner5", "abacate", "calopcita", 22);

		Boolean resultado = userServi.excluir(usuario.getId(), "abacate");		
		assertThat(resultado).describedAs("Usuario não foi excluido").isTrue();

	}
	
	@Test
	@DisplayName("Excluir: Ao tentar excluir, mas com a senha errada deve retornar false")
	void FalharExcluir() throws Exception {
		
		var usuario = userServi.cadastrar("Alex Turner6", "abacate", "calopcita", 22);

		Boolean resultado = userServi.excluir(usuario.getId(), "shangrila");		
		assertThat(resultado).describedAs("Metodo retornou que o usuário foi excluido, algo deu errado").isFalse();

	}
	
}



