package com.LSoftwareLTDA.diarioDigital.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.LSoftwareLTDA.diarioDigital.entidades.Livro;
import com.LSoftwareLTDA.diarioDigital.entidades.Usuario;
import com.LSoftwareLTDA.diarioDigital.excecoes.GerenciamentoLivroException;
import com.LSoftwareLTDA.diarioDigital.repositorios.UsuarioRepositorio;
import com.LSoftwareLTDA.diarioDigital.service.LivrosService;

@ComponentScan(basePackages = "com.LSoftwareLTDA.diarioDigital")

@SpringBootTest
@ActiveProfiles("test")
class LivrosServiceTest {

	@Autowired
	LivrosService livroServi;
	
	@Autowired
	 UsuarioRepositorio userRepo;
	
	@Test
	@DisplayName("Criando livro com sucesso")
	void criarLivroSucesso() throws Exception {
		
		Usuario usuario = new Usuario("Caio", "1234", "jaguatirica", 18);
		
		usuario = userRepo.save(usuario);
		
		var livro = livroServi.criarLivro("Meia noite eu te conto", usuario.getId());
		
		assertThat(livro).describedAs("Livro retornou nulo").isNotNull();
		assertThat(livro.getId()).describedAs("Id do livro não foi adicionado, retornou nulo").isNotNull();
		assertThat(livro.getUsuario().getId()).describedAs("Id do usuario do livro não bate com o ultilizado para a criação").isEqualTo(usuario.getId());


	}
	
	@Test
	@DisplayName("Tentar criando livro que já foi criado")
	void criarLivroCriado() throws Exception {
		
		 Usuario usuario = new Usuario("Caio2", "1234", "jaguatirica", 18);
		
		usuario = userRepo.save(usuario);
		
		String  titulo = "Meia noite eu te conto2 o inimigo agora é outro";
		final UUID id = usuario.getId();
		
		livroServi.criarLivro(titulo, id );
		
		assertThrows(GerenciamentoLivroException.class, () ->{
			livroServi.criarLivro(titulo, id);
			}, "Excessão que deveria ser lançada ao tentar criar um livro já criado não foi criado");
		

	}
	
	@Test
	@DisplayName("Tentar criando livro com id de usuario null")
	void criarLivroIdUsuarioNull() {
		
		String  titulo = "Meia noite eu te conto2 o inimigo agora é outro";
		
		assertThrows(NullPointerException.class, () ->{
			livroServi.criarLivro(titulo, null);
			}, "Excessão que deveria ser lançada ao tentar criar um livro com id de usuario null não foi lançada");

	}

	@Test
	@DisplayName("Consultar livro com sucesso")
	void consultarLivroSucesso() throws Exception {
		
		Usuario usuario = new Usuario("Caio3", "1234", "jaguatirica", 18);
		
		usuario = userRepo.save(usuario);
		
		String titulo = "Meia noite eu te conto 3";
		
		livroServi.criarLivro(titulo, usuario.getId());
		
		Livro livro = livroServi.consultarLivro(titulo, usuario.getId());
	
		assertThat(livro).describedAs("Livro retornou nulo").isNotNull();
		assertThat(livro.getId()).describedAs("Id do livro não foi adicionado, retornou nulo").isNotNull();
		assertThat(livro.getUsuario().getId()).describedAs("Id do usuario do livro não bate com o ultilizado para a criação").isEqualTo(usuario.getId());

	}
	
	@Test
	@DisplayName("Tentar consultar livro não cadastrado")
	
	void consultarLivroNaoCadastrado() {
		Usuario usuario = new Usuario("Caio4", "1234", "jaguatirica", 18);
		
		usuario = userRepo.save(usuario);
		
		String  titulo = "Meia noite eu te conto4 o inimigo agora é outro";
		UUID id = usuario.getId();
		
		assertThrows(IllegalArgumentException.class, () ->{
			livroServi.consultarLivro(titulo, id);
			}, "Excessão que deveria ser lançada ao tentar criar um livro já criado não foi criado");

	}

	@Test
	@DisplayName("Tentar consultar livro com id de usuario null")
	void consultarLivroIdUsuarioNull() {
		
		String  titulo = "Meia noite eu te conto2 o inimigo agora é outro";
		
		assertThrows(NullPointerException.class, () ->{
			livroServi.consultarLivro(titulo, null);
			}, "Excessão que deveria ser lançada ao tentar consultar um livro com id de usuario null não foi lançada");

	}

	@Test
	@DisplayName("Excluir livro com sucesso")
	void excluirSucesso() throws Exception {
		Usuario usuario = new Usuario("Caio7", "1234", "jaguatirica", 18);
		
		usuario = userRepo.save(usuario);
		
		var livro = livroServi.criarLivro("Meia noite eu te conto", usuario.getId());
		
		Boolean resultado = livroServi.excluirLivro(livro.getId(),usuario.getId(), usuario.getSenha());
		assertThat(resultado).describedAs("Usuario não foi excluido").isTrue();

	}
	
	@Test
	@DisplayName("Excluir livro com id de usuario null")
	void excluirLivroUsuarioNull() throws Exception {
		Usuario usuario = new Usuario("Caio8", "1234", "jaguatirica", 18);
		
		usuario = userRepo.save(usuario);
		
		var livro = livroServi.criarLivro("Meia noite eu te conto", usuario.getId());
		
		Boolean resultado = livroServi.excluirLivro(livro.getId(),null, usuario.getSenha());
		assertThat(resultado).describedAs("Usuario não foi excluido").isFalse();

	}
	
	@Test
	@DisplayName("Excluir livro com senha null")
	void excluirSsenhaNull() throws Exception {
		Usuario usuario = new Usuario("Caio7", "1234", "jaguatirica", 18);
		
		usuario = userRepo.save(usuario);
		
		var livro = livroServi.criarLivro("Meia noite eu te conto", usuario.getId());
		
		Boolean resultado = livroServi.excluirLivro(livro.getId(),usuario.getId(), null);
		assertThat(resultado).describedAs("Usuario não foi excluido").isFalse();

	}
}
