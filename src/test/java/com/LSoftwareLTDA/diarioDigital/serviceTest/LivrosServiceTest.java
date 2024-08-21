package com.LSoftwareLTDA.diarioDigital.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.LSoftwareLTDA.diarioDigital.controller.dto.LivroDTO;
import com.LSoftwareLTDA.diarioDigital.controller.dto.UsuarioDTO;
import com.LSoftwareLTDA.diarioDigital.entidades.Livro;
import com.LSoftwareLTDA.diarioDigital.entidades.Usuario;
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

	@Test
	@DisplayName("Criando livro com sucesso")
	void criarLivroSucesso() {

		UsuarioDTO usuario = criacaoUsuario("Caio3");
		var livro = livroServi.criarLivro("Meia noite eu te conto", usuario.getId());

		assertThat(livro).describedAs("Livro retornou nulo").isNotNull();
		userServi.excluirUsuario(usuario);
	}

	@Test
	@DisplayName("Tentar criando livro que já foi criado")
	void criarLivroCriado() {

		UsuarioDTO usuario = criacaoUsuario("Caio4");
		String titulo = "Meia noite eu te conto2 o inimigo agora é outro";
		livroServi.criarLivro(titulo, usuario.getId());

		assertThrows(CadastroNegadoException.class, () -> {
			livroServi.criarLivro(titulo, usuario.getId());
		}, "Deveria ter lançado CadastroNegadoException");
		excluirUsuario(usuario);
		

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

		UsuarioDTO usuario = criacaoUsuario("Caio5");
		String titulo = "Meia noite eu te conto 4 e a guerra dos clones";
		
		livroServi.criarLivro(titulo, usuario.getId());
		LivroDTO livro = livroServi.consultarLivro(titulo, usuario.getId());

		assertThat(livro).describedAs("Livro retornou nulo").isNotNull();
		excluirUsuario(usuario);
	}

	@Test
	@DisplayName("Tentar consultar livro não cadastrado")

	void consultarLivroNaoCadastrado() {

		UsuarioDTO usuario = criacaoUsuario("Caio6");
		String titulo = "Meia noite eu te conto 5 o imperio contra ataca";

		assertThrows(EntidadeNaoEncontrada.class, () -> {
			livroServi.consultarLivro(titulo, usuario.getId());
		}, "Excessão que deveria ser lançada ao tentar criar um livro já criado não foi criado");
		excluirUsuario(usuario);

	}

	@Test
	@DisplayName("Tentar consultar livro com id de usuario null")
	void consultarLivroIdUsuarioNull() {

		String titulo = "Meia noite eu te conto6 a era de ultron";

		assertThrows(EntidadeNaoEncontrada.class, () -> {
			livroServi.consultarLivro(titulo, new UsuarioDTO().getId());
		}, "Excessão que deveria ser lançada ao tentar consultar um livro com id de usuario null não foi lançada");

	}

	@Test
	@DisplayName("Excluir livro com sucesso")
	void excluirSucesso() {

		UsuarioDTO usuario = criacaoUsuario("Caio7");
		var livro = livroServi.criarLivro("Meia noite eu te conto6 new vegas", usuario.getId());
		Boolean resultado = livroServi.excluirLivro(livro.getId(), usuario.getId(), usuario.getSenha());

		assertThat(resultado).describedAs("Usuario não foi excluido").isTrue();
		
		excluirUsuario(usuario);
	}

	@Test
	@DisplayName("Excluir livro com id de usuario null")
	void excluirLivroUsuarioNull() {

		UsuarioDTO usuario = criacaoUsuario("Caio8");
		var livro = livroServi.criarLivro("Meia noite eu te conto8 não lembro de mais nenhum nome de filme",
				usuario.getId());

		assertThrows(EntidadeNaoEncontrada.class, () -> {
			livroServi.excluirLivro(livro.getId(), null, usuario.getSenha());
		}, "exceção de entidade não encontrada não foi lançada");
		
		excluirUsuario(usuario);
	}

	@Test
	@DisplayName("Excluir livro com senha null")
	void excluirSsenhaNull() {

		UsuarioDTO usuario = criacaoUsuario("Caio10");
		var livro = livroServi.criarLivro("Meia noite eu te conto9 ainda não tenho trocadilho novo", usuario.getId());

		assertThrows(PermissaoNegadaException.class, () -> {
			livroServi.excluirLivro(livro.getId(), usuario.getId(), null);
		}, "exceção de entidade não encontrada não foi lançada");
		
		excluirUsuario(usuario);
	}

	private UsuarioDTO criacaoUsuario(String nome) {
		UsuarioDTO dto = new UsuarioDTO(nome, "1234", "jaguatirica", 18);

		return userServi.cadastrarUsuario(dto);
	}

	private void excluirUsuario(UsuarioDTO usuario) {

		userServi.excluirUsuario(usuario);
	}

}
