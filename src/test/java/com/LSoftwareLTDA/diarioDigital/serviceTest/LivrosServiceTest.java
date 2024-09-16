package com.LSoftwareLTDA.diarioDigital.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.LSoftwareLTDA.diarioDigital.controller.dto.livro.response.LivroResponse;
import com.LSoftwareLTDA.diarioDigital.controller.dto.usuario.requisicoes.CadastroRequest;
import com.LSoftwareLTDA.diarioDigital.controller.dto.usuario.resposta.UsuarioResponse;
import com.LSoftwareLTDA.diarioDigital.service.LivrosService;
import com.LSoftwareLTDA.diarioDigital.service.UserService;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.CadastroNegadoException;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.EntidadeNaoEncontrada;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.PermissaoNegadaException;

@ComponentScan(basePackages = "com.LSoftwareLTDA.diarioDigital")

@SpringBootTest
@ActiveProfiles("test")
class LivrosServiceTest {

	@Autowired
	LivrosService livroServi;

	@Autowired
	UserService userServi;
	
	private String senha = "1234";

	@Test
	@DisplayName("Criando livro com sucesso")
	void criarLivroSucesso() {

		UsuarioResponse usuario = criacaoUsuario("Caio3");
		var livro = livroServi.criarLivro("Meia noite eu te conto", usuario.id());

		assertThat(livro).describedAs("Livro retornou nulo").isNotNull();

		excluirUsuario(usuario.id());

	}

	@Test
	@DisplayName("Tentar criando livro que já foi criado")
	void criarLivroCriado() {

		UsuarioResponse usuario = criacaoUsuario("Caio4");
		String titulo = "Meia noite eu te conto2 o inimigo agora é outro";
		livroServi.criarLivro(titulo, usuario.id());

		assertThrows(CadastroNegadoException.class, () -> {
			livroServi.criarLivro(titulo, usuario.id());
		}, "Deveria ter lançado CadastroNegadoException");

		excluirUsuario(usuario.id());

	}

	@Test
	@DisplayName("Tentar criando livro com id de usuario null")
	void criarLivroIdUsuarioNull() {

		String titulo = "Meia noite eu te conto3 e a  ameaça fantasma";

		assertThrows(PermissaoNegadaException.class, () -> {
			livroServi.criarLivro(titulo, null);
		}, "Devido ao id null deveria ter lançado PermissaoNegadaException");

	}

	@Test
	@DisplayName("Tentar criando livro com id de usuario não cadastrado")
	void criarLivroIdUsuarioInexistente() {

		String titulo = "Meia noite eu te conto4 e a  ameaça fantasma";

		assertThrows(EntidadeNaoEncontrada.class, () -> {
			livroServi.criarLivro(titulo, 573634L);
		}, "Devido ao id inexistente deveria ter lançado EntidadeNaoEncontrada");

	}

	@Test
	@DisplayName("Consultar livro com sucesso")
	void consultarLivroSucesso() {

		UsuarioResponse usuario =  criacaoUsuario("Caio5");
		String titulo = "Meia noite eu te conto 4 e a guerra dos clones";

		LivroResponse resposta = livroServi.criarLivro(titulo, usuario.id());
		LivroResponse livro = livroServi.consultarLivro(resposta.id(), usuario.id());

		assertThat(livro).describedAs("Livro retornou nulo").isNotNull();
		excluirUsuario(usuario.id());

	}

	@Test
	@DisplayName("Tentar consultar livro não cadastrado")

	void consultarLivroNaoCadastrado() {

		UsuarioResponse usuario =  criacaoUsuario("Caio6");

		assertThrows(EntidadeNaoEncontrada.class, () -> {
			livroServi.consultarLivro(7934l, usuario.id());
		}, "Excessão que deveria ser lançada ao tentar criar um livro já criado não foi criado");
		excluirUsuario(usuario.id());

	}

	@Test
	@DisplayName("Tentar consultar livro com id de usuario null")
	void consultarLivroIdUsuarioNull() {

		assertThrows(EntidadeNaoEncontrada.class, () -> {
			livroServi.consultarLivro(1l, null);
		}, "Excessão que deveria ser lançada ao tentar consultar um livro com id de usuario null não foi lançada");

	}

	@Test
	@DisplayName("Excluir livro com sucesso")
	void excluirSucesso() {

		UsuarioResponse usuario =  criacaoUsuario("Caio7");
		LivroResponse livro = livroServi.criarLivro("Meia noite eu te conto6 new vegas", usuario.id());
		Boolean resultado = livroServi.excluirLivro(livro.id(), usuario.id(), senha);

		assertThat(resultado).describedAs("Usuario não foi excluido").isTrue();

		excluirUsuario(usuario.id());

	}

	@Test
	@DisplayName("Excluir livro com id de usuario null")
	void excluirLivroUsuarioNull() {

		UsuarioResponse usuario =  criacaoUsuario("Caio8");
		LivroResponse livro = livroServi.criarLivro("Meia noite eu te conto8 não lembro de mais nenhum nome de filme",
				usuario.id());

		assertThrows(EntidadeNaoEncontrada.class, () -> {
			livroServi.excluirLivro(livro.id(), null, senha);
		}, "exceção de entidade não encontrada não foi lançada");

		excluirUsuario(usuario.id());

	}

	@Test
	@DisplayName("Excluir livro com senha null")
	void excluirSsenhaNull() {

		UsuarioResponse usuario =  criacaoUsuario("Caio10");
		LivroResponse livro = livroServi.criarLivro("Meia noite eu te conto9 ainda não tenho trocadilho novo", usuario.id());

		assertThrows(PermissaoNegadaException.class, () -> {
			livroServi.excluirLivro(livro.id(), usuario.id(), null);
		}, "exceção de entidade não encontrada não foi lançada");

		excluirUsuario(usuario.id());

	}

	private UsuarioResponse criacaoUsuario(String nome) {
		CadastroRequest request =  new CadastroRequest(nome, senha, "jaguatirica", 18);

		return userServi.cadastrarUsuario(request);
	}

	private void excluirUsuario(Long id) {
		userServi.excluirUsuario(id, "1234");
	}

}
