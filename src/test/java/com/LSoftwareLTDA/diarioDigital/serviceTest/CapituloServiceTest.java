package com.LSoftwareLTDA.diarioDigital.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.LSoftwareLTDA.diarioDigital.controller.dto.CapituloDTO;
import com.LSoftwareLTDA.diarioDigital.controller.dto.UsuarioDTO;
import com.LSoftwareLTDA.diarioDigital.entidades.Livro;
import com.LSoftwareLTDA.diarioDigital.entidades.Usuario;
import com.LSoftwareLTDA.diarioDigital.service.CapituloService;
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

	@Test
	@DisplayName("Criar capitulos com sucesso")
	void CriarCapituloSucesso() {
		
		Livro livro = criacaoLivro("Noel", "tururu");
		
		CapituloDTO dto = new CapituloDTO("bom dia", "123", 1, livro.getId(),livro.getUsuario().getId());
		
		CapituloDTO capitulo =capServi.criarCapitulo(dto);
		
		assertThat(capitulo).describedAs("capitulo não foi criado").isNotNull();
		
		excluirUsuario(livro.getUsuario());
	}
	
	@Test
	@DisplayName("Tentar criar capitulos com titulo repetido para o mesmo livro do mesmo dono")
	void CriarCapituloComtituloRepetido() {
		
		Livro livro = criacaoLivro("Noel 2", "tururu2");
		
		CapituloDTO dto = new CapituloDTO("boa tarde", "123", 1, livro.getId(),livro.getUsuario().getId());
		
		CapituloDTO capitulo =capServi.criarCapitulo(dto);
		
		assertThrows(CadastroNegadoException.class, ()->{
			capServi.criarCapitulo(dto);

		},"Cadastro de capitulo já cadastrado no mesmo livro aconteceu.");
		
		excluirUsuario(livro.getUsuario());

	}
	
	@Test
	@DisplayName("Tentar criar capitulos com livro e usuario invalidos")
	void CriarCapituloLivroUsaurioInvalidos() {
		
		Livro livro = criacaoLivro("Noel 3", "tururu3");
		
		CapituloDTO dto = new CapituloDTO("bom noite", "123", 1, 9828L,8023L);
				
		assertThrows(EntidadeNaoEncontrada.class, ()->{
			capServi.criarCapitulo(dto);

		},"não deveria haver um usuario e livro com os ids citados.");
		
		excluirUsuario(livro.getUsuario());

	}
	
	@Test
	@DisplayName("Tentar criar capitulos com conteudo em branco ou nulo")
	void CriarCapituloConteudoNulo() {
		
		Livro livro = criacaoLivro("Noel 4", "tururu4");
		
		CapituloDTO dto = new CapituloDTO("bom meia noite", "", 1, livro.getId(),livro.getUsuario().getId());
		

		
		assertThrows(CadastroNegadoException.class, ()->{
			capServi.criarCapitulo(dto);

		},"Cadastro de capitulo sem conteudo aconteceu.");
		
		excluirUsuario(livro.getUsuario());

	}
	
	@Test
	@DisplayName("Tentar criar capitulos com identificadores nulos")
	void CriarCapituloIDsNulos() {
		
		Livro livro = criacaoLivro("Noel 5", "tururu5");
		
		CapituloDTO dto = new CapituloDTO("boa manha", "123", 1,  livro.getId(),livro.getUsuario().getId());
		dto =capServi.criarCapitulo(dto);
		dto.setIdLivro(null);
		dto.setIdUsuario(null);
		CapituloDTO capitulo = dto;
		
		assertThrows(EntidadeNaoEncontrada.class, ()->{
			capServi.criarCapitulo(capitulo);

		},"Cadastro de capitulo já cadastrado no mesmo livro aconteceu.");
		
		excluirUsuario(livro.getUsuario());

	}

	// consultar
	
	@Test
	@DisplayName("Consultar capitulo com sucesso")
	void ConsultarCapituloSucesso() {
		
		Livro livro = criacaoLivro("Noel 6", "tururu 6");
		
		CapituloDTO dto = new CapituloDTO("bom meio dia", "123", 1, livro.getId(),livro.getUsuario().getId());
		
		 dto =capServi.criarCapitulo(dto);
		 
		 CapituloDTO capitulo = capServi.consultarCapitulo(dto);
		
		assertThat(capitulo).describedAs("capitulo foi criado").isNotNull();
		
		excluirUsuario(livro.getUsuario());
	}

	@Test
	@DisplayName("Consultar capitulo que não existe")
	void ConsultarCapituloCapInexistente() {
		
		Livro livro = criacaoLivro("Noel 7", "tururu 7");
		
		CapituloDTO dto = new CapituloDTO("bom dia2", "123", 1, livro.getId(),livro.getUsuario().getId());
		
		dto.setId(6788L);
				
		 assertThrows(EntidadeNaoEncontrada.class, ()->{
				capServi.consultarCapitulo(dto);

			},"teste falhou.");
			
		
		excluirUsuario(livro.getUsuario());
	}
	
	@Test
	@DisplayName("Consultar capitulo com id nulo")
	void ConsultarCapituloIdNulo() {
		
		
		CapituloDTO dto = new CapituloDTO();
		
		 assertThrows(PermissaoNegadaException.class, ()->{
				capServi.consultarCapitulo(dto);

			},"teste falhou.");
		 
	}
	
	// excluir
	
	@Test
	@DisplayName("Excluir capitulos com sucesso")
	void ExcluirCapituloSucesso() {
		
		Livro livro = criacaoLivro("Noel9", "tururu9");
		
		CapituloDTO dto = new CapituloDTO("bom dia3", "123", 1, livro.getId(),livro.getUsuario().getId());
		
		CapituloDTO capitulo =capServi.criarCapitulo(dto);
		capitulo.setIdUsuario(dto.getIdUsuario());
		capitulo.setIdLivro(dto.getIdLivro());
		
		assertThat(capServi.excluirCapitulo(capitulo)).describedAs("capitulo não foi excluido").isTrue();
		
		excluirUsuario(livro.getUsuario());
	}
	
	@Test
	@DisplayName("Falhar ao excluir capitulo")
	void FalharExcluirCapitulo() {
		
		Livro livro = criacaoLivro("Noel 10", "tururu 10");
		
		CapituloDTO dto = new CapituloDTO("bom dia3", "123", 1, livro.getId(),livro.getUsuario().getId());
		
		dto.setId(6788L);
				
		 assertThrows(EntidadeNaoEncontrada.class, ()->{
				capServi.excluirCapitulo(dto);

			},"teste falhou.");
		 
			excluirUsuario(livro.getUsuario());

		 }
	
	@Test
	@DisplayName("falhar ao excluir capitulo com identificadores nulos")
	void FalharExcluirCapituloIdentificadoresNulos() {
		
		Livro livro = criacaoLivro("Noel 11", "tururu 11");
		
		CapituloDTO dto = new CapituloDTO("bom dia3", "123", 1, livro.getId(),livro.getUsuario().getId());
		
		dto =capServi.criarCapitulo(dto);
		dto.setIdLivro(null);
		dto.setIdUsuario(null);
		CapituloDTO capitulo = dto;
				
		 assertThrows(EntidadeNaoEncontrada.class, ()->{
				capServi.excluirCapitulo(capitulo);

			},"teste falhou.");
		 
			excluirUsuario(livro.getUsuario());

		 }
	
	
	private Livro criacaoLivro(String nome, String livro) {
		UsuarioDTO dto = new UsuarioDTO(nome, "1234", "jaguatirica", 18);
		
		dto = userServi.cadastrarUsuario(dto);
		
		Usuario usuario = new Usuario(dto);
		
		Livro li = new Livro(livro, usuario);
		
		List<Livro> livros= usuario.getLivros();
		
		livros.add(li);
		
		usuario.setLivros(livros);
		
		dto = new UsuarioDTO(usuario);
		
		dto = userServi.cadastrarUsuario(dto);
		
		return  dto.getLivros().get(0);
		
	}

	private void excluirUsuario(Usuario usuario) {

		userServi.excluirUsuario(new UsuarioDTO(usuario));
	}

}
