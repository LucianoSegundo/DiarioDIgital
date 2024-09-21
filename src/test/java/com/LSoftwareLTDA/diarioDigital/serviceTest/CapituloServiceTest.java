package com.LSoftwareLTDA.diarioDigital.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.LSoftwareLTDA.diarioDigital.controller.dto.capitulos.CapituloRequest;
import com.LSoftwareLTDA.diarioDigital.controller.dto.capitulos.CapituloResponse;
import com.LSoftwareLTDA.diarioDigital.controller.dto.livro.response.LivroResponse;
import com.LSoftwareLTDA.diarioDigital.controller.dto.usuario.requisicoes.CadastroRequest;
import com.LSoftwareLTDA.diarioDigital.controller.dto.usuario.resposta.UsuarioResponse;
import com.LSoftwareLTDA.diarioDigital.entidades.Livro;
import com.LSoftwareLTDA.diarioDigital.entidades.Usuario;
import com.LSoftwareLTDA.diarioDigital.service.CapituloService;
import com.LSoftwareLTDA.diarioDigital.service.LivrosService;
import com.LSoftwareLTDA.diarioDigital.service.UserService;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.CadastroNegadoException;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.EntidadeNaoEncontrada;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.PermissaoNegadaException;

@ComponentScan(basePackages = "com.LSoftwareLTDA.diarioDigital")

@SpringBootTest
@ActiveProfiles("test")
class CapituloServiceTest {

	@Autowired
	private CapituloService capServi;

	@Autowired
	private UserService userServi;

	@Autowired
	private LivrosService livroServi;

	private String senha = "1234";

	@Test
	@DisplayName("Criar capitulos com sucesso")
	void CriarCapituloSucesso() {

		Livro livro = criacaoLivro("Noel", "tururu");

		CapituloRequest request = new CapituloRequest("bom dia", "123");

		CapituloResponse resposta = capServi.criarCapitulo(request, livro.getId(), livro.getUsuario().getId());

		assertThat(resposta).describedAs("capitulo não foi criado").isNotNull();

		excluirUsuario(livro.getUsuario());
	}

	@Test
	@DisplayName("Tentar criar capitulos com titulo repetido para o mesmo livro do mesmo dono")
	void CriarCapituloComtituloRepetido() {

		Livro livro = criacaoLivro("Noel 2", "tururu2");

		CapituloRequest request = new CapituloRequest("boa tarde", "123");

		CapituloResponse resposta = capServi.criarCapitulo(request, livro.getId(), livro.getUsuario().getId());

		assertThrows(CadastroNegadoException.class, () -> {
			capServi.criarCapitulo(request, livro.getId(), livro.getUsuario().getId());

		}, "Cadastro de capitulo já cadastrado no mesmo livro aconteceu.");

		excluirUsuario(livro.getUsuario());

	}

	@Test
	@DisplayName("Tentar criar capitulos com livro e usuario invalidos")
	void CriarCapituloLivroUsaurioInvalidos() {

		Livro livro = criacaoLivro("Noel 3", "tururu3");

		CapituloRequest request = new CapituloRequest("bom noite", "123");

		assertThrows(EntidadeNaoEncontrada.class, () -> {
			capServi.criarCapitulo(request, 12345L, 12345L);

		}, "não deveria haver um usuario e livro com os ids citados.");

		excluirUsuario(livro.getUsuario());

	}

	@Test
	@DisplayName("Tentar criar capitulos com conteudo em branco ou nulo")
	void CriarCapituloConteudoNulo() {

		Livro livro = criacaoLivro("Noel 4", "tururu4");

		CapituloRequest request = new CapituloRequest("bom meia noite", "");

		assertThrows(CadastroNegadoException.class, () -> {
			capServi.criarCapitulo(request, livro.getId(), livro.getUsuario().getId());

		}, "Cadastro de capitulo sem conteudo aconteceu.");

		excluirUsuario(livro.getUsuario());

	}

	@Test
	@DisplayName("Tentar criar capitulos com identificadores nulos")
	void CriarCapituloIDsNulos() {

		Livro livro = criacaoLivro("Noel 5", "tururu5");

		CapituloRequest request = new CapituloRequest("boa manha", "123");

		assertThrows(EntidadeNaoEncontrada.class, () -> {
			capServi.criarCapitulo(request, null, null);

		}, "Cadastro de capitulo já cadastrado no mesmo livro aconteceu.");

		excluirUsuario(livro.getUsuario());

	}

	// consultar

	@Test
	@DisplayName("Consultar capitulo com sucesso")
	void ConsultarCapituloSucesso() {

		Livro livro = criacaoLivro("Noel 6", "tururu 6");

		CapituloRequest request = new CapituloRequest("bom meio dia", "123");

		CapituloResponse resposta = capServi.criarCapitulo(request, livro.getId(), livro.getUsuario().getId());

		resposta = capServi.consultarCapitulo(livro.getId(), resposta.id());

		assertThat(resposta).describedAs("capitulo foi criado").isNotNull();

		excluirUsuario(livro.getUsuario());
	}

	@Test
	@DisplayName("Consultar capitulo que não existe")
	void ConsultarCapituloCapInexistente() {

		Livro livro = criacaoLivro("Noel 7", "tururu 7");

		assertThrows(EntidadeNaoEncontrada.class, () -> {
			capServi.consultarCapitulo(livro.getId(), 123456L);

		}, "teste falhou.");

		excluirUsuario(livro.getUsuario());
	}

	@Test
	@DisplayName("Consultar capitulo com id nulo")
	void ConsultarCapituloIdNulo() {
		Livro livro = criacaoLivro("Noel 90", "tururu 90");


		assertThrows(PermissaoNegadaException.class, () -> {
			capServi.consultarCapitulo(livro.getId(), null);

		}, "teste falhou.");

		excluirUsuario(livro.getUsuario());
	}

	// excluir

	@Test
	@DisplayName("Excluir capitulos com sucesso")
	void ExcluirCapituloSucesso() {

		Livro livro = criacaoLivro("Noel9", "tururu9");

		CapituloRequest request = new CapituloRequest("bom dia3", "123");

		CapituloResponse resposta = capServi.criarCapitulo(request, livro.getId(), livro.getUsuario().getId());

		assertThat(capServi.excluirCapitulo(resposta.id(), livro.getId(), livro.getUsuario().getId()))
				.describedAs("capitulo não foi excluido").isTrue();

		excluirUsuario(livro.getUsuario());
	}

	@Test
	@DisplayName("Falhar ao excluir capitulo")
	void FalharExcluirCapitulo() {

		Livro livro = criacaoLivro("Noel 10", "tururu 10");

		assertThrows(EntidadeNaoEncontrada.class, () -> {
			capServi.excluirCapitulo(12345L, livro.getId(), livro.getUsuario().getId());
		}, "teste falhou.");

		excluirUsuario(livro.getUsuario());

	}

	@Test
	@DisplayName("falhar ao excluir capitulo com identificadores nulos")
	void FalharExcluirCapituloIdentificadoresNulos() {

		Livro livro = criacaoLivro("Noel 11", "tururu 11");

		CapituloRequest request = new CapituloRequest("bom dia3", "123");

		CapituloResponse resposta = capServi.criarCapitulo(request, livro.getId(), livro.getUsuario().getId());

		assertThrows(EntidadeNaoEncontrada.class, () -> {
			capServi.excluirCapitulo(resposta.id(), null, null);

		}, "teste falhou.");

		excluirUsuario(livro.getUsuario());

	}

	private Livro criacaoLivro(String nome, String livro) {
		CadastroRequest request = new CadastroRequest(nome, "1234", "jaguatirica", 18);

		UsuarioResponse respos = userServi.cadastrarUsuario(request);

		LivroResponse dto = livroServi.criarLivro(livro, respos.id());

		Usuario user = new Usuario(request);
		user.setId(respos.id());

		Livro resposta = new Livro(dto);
		resposta.setUsuario(user);

		return resposta;

	}

	private void excluirUsuario(Usuario usuario) {

		userServi.excluirUsuario(usuario.getId(), senha);
	}

}
