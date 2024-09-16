package com.LSoftwareLTDA.diarioDigital.service;

import java.time.Instant;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.LSoftwareLTDA.diarioDigital.controller.dto.usuario.requisicoes.CadastroRequest;
import com.LSoftwareLTDA.diarioDigital.controller.dto.usuario.requisicoes.loginRequest;
import com.LSoftwareLTDA.diarioDigital.controller.dto.usuario.resposta.UsuarioResponse;
import com.LSoftwareLTDA.diarioDigital.controller.dto.usuario.resposta.loginResponse;
import com.LSoftwareLTDA.diarioDigital.entidades.Usuario;
import com.LSoftwareLTDA.diarioDigital.interfaces.RecuperacaoSenha;
import com.LSoftwareLTDA.diarioDigital.repositorios.UsuarioRepositorio;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.CadastroNegadoException;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.EntidadeNaoEncontrada;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.PermissaoNegadaException;
import com.LSoftwareLTDA.diarioDigital.service.excecoes.TokenInvalido;

import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Service
public class UserService {

	private UsuarioRepositorio userRepo;
	private RecuperacaoSenha recuperadorSenha;
	private BCryptPasswordEncoder codificadorSenha;
	private JwtEncoder jwtCodificador;

	public UserService(UsuarioRepositorio repositorio, RecuperacaoSenha recuperador,
			BCryptPasswordEncoder codificadorSenha, JwtEncoder codificador) {
		this.userRepo = repositorio;
		this.recuperadorSenha = recuperador;
		this.codificadorSenha = codificadorSenha;
		this.jwtCodificador = codificador;
		System.out.println("Bean UserService criado");
	}

	@Transactional
	public UsuarioResponse cadastrarUsuario(CadastroRequest request) {

		try {
			// lembrar de criptografar a senha posteriormente.
			if (request.senha() == null || request.palavraSegu() == null)
				throw new PermissaoNegadaException("Parametros necessários estão vazios");

			Usuario entidade = new Usuario(request);

			String codigo = codificadorSenha.encode(entidade.getSenha());
			String codigo2 = codificadorSenha.encode(entidade.getPalavraSegu());

			entidade.setSenha(codigo);
			entidade.setPalavraSegu(codigo2);

			UsuarioResponse resposta = new UsuarioResponse(userRepo.save(entidade));
			return resposta;

		} catch (DataIntegrityViolationException e) {
			throw new CadastroNegadoException("Usuario já cadastrado");
		} catch (ConstraintViolationException e) {
			throw new PermissaoNegadaException("Parametros necessários estão vazios");
		}

	};

	@Transactional
	public Boolean excluirUsuario(Long id, String senha) {
		try {

			if (id == null || senha == null)
				throw new PermissaoNegadaException("Não foi possivel excluir usuario id do usuario não pode ser null");

			var user = userRepo.findById(id);

			Usuario usuario = user.orElseThrow(
					() -> new EntidadeNaoEncontrada("Usuario não foi encontrado, não é possivel excluilo"));

			if (codificadorSenha.matches(senha, usuario.getSenha())) {
				userRepo.delete(usuario);
				return true;
			} else
				throw new PermissaoNegadaException("Não foi permitido a exclussão deste recurso");

		} catch (DataIntegrityViolationException e) {
			throw new PermissaoNegadaException("Não foi possivel excluir usuario ");
		} catch (InvalidDataAccessApiUsageException e) {
			throw new PermissaoNegadaException("Não foi possivel excluir usuario id do usuario não pode ser null");
		}
	};

	@Transactional(readOnly = true)
	public UsuarioResponse consultar(Long id) {

		try {
			var user = userRepo.findById(id);
			Usuario resposta = user.orElseThrow(() -> new EntidadeNaoEncontrada("A Usuario não foi encontrado"));

			return new UsuarioResponse(resposta);

		} catch (InvalidDataAccessApiUsageException e) {
			throw new PermissaoNegadaException("Operação de consulta não foi permitida");
		}
	};

	@Transactional(readOnly = true)
	public Page<UsuarioResponse> consultarUsuarios(PageRequest pagina) {

		var lista = userRepo.findAll(pagina);

		return lista.map(x -> {
			UsuarioResponse resposta = new UsuarioResponse(x);
			return resposta;
		});

	}

	@Transactional
	public UsuarioResponse trocarSenha(Long id, String novasenha, String palavra) {

		try {
			if (novasenha == null || id == null || palavra == null)
				throw new PermissaoNegadaException("não foi possivel trocara senha");

			var user = userRepo.findById(id);

			Usuario usuario = user.orElseThrow(() -> new EntidadeNaoEncontrada("Usuario não encontrado"));

			if (codificadorSenha.matches(palavra, usuario.getPalavraSegu())) {

				novasenha = codificadorSenha.encode(novasenha);
				usuario.setSenha(novasenha);
				UsuarioResponse resposta = new UsuarioResponse(userRepo.save(usuario));
				return resposta;

			} else
				throw new PermissaoNegadaException("Acesso negado, troca de senha não permitida");

		} catch (DataIntegrityViolationException e) {
			throw new PermissaoNegadaException(e.getMessage());
		} catch (InvalidDataAccessApiUsageException e) {
			throw new PermissaoNegadaException("não foi possivel trocara senha");
		}
	};

	@Transactional
	public UsuarioResponse recuperarSenha(String nome, String novasenha, String palavra) {
		try {
			if (novasenha == null || nome == null || palavra == null)
				throw new PermissaoNegadaException("não foi possivel trocara senha");

			var user = userRepo.findByNome(nome);

			Usuario usuario = user.orElseThrow(() -> new EntidadeNaoEncontrada("Usuario não encontrado"));

			UsuarioResponse resposta = trocarSenha(usuario.getId(), novasenha, palavra);
			return resposta;
		} catch (InvalidDataAccessApiUsageException e) {
			throw new PermissaoNegadaException("não foi possivel trocara senha");
		}
	}

	@Transactional(readOnly = true)

	public loginResponse logar(loginRequest dto) {

		try {
			var user = userRepo.findByNome(dto.nome());

			// Lembrar de descriptografar a senha antes da comparação.

			Usuario usuario = user
					.orElseThrow(() -> new EntidadeNaoEncontrada("Usuario com tau nome não foi encontrado"));
			String senhaSalva = usuario.getSenha();

			if (codificadorSenha.matches(dto.senha(), senhaSalva)) {
				return gerarToken(usuario);
			} else
				throw new PermissaoNegadaException("Acesso negado");

		} catch (InvalidDataAccessApiUsageException e) {
			throw new PermissaoNegadaException("Acesso negado, o nome do usuarío se encontra em branco ou nulo");
		}

	}

	private loginResponse gerarToken(Usuario usuario) {
		Instant now = Instant.now();
		Long expiresIn = 300L;
		var scopes = usuario.getRole().toString();

		var claims = JwtClaimsSet.builder().issuer("DiarioDigital").subject(usuario.getId().toString())
				.expiresAt(now.plusSeconds(expiresIn)).claim("scape", scopes).build();
		var jwt = jwtCodificador.encode(JwtEncoderParameters.from(claims)).getTokenValue();
		return new loginResponse(jwt, expiresIn);

	}

	@Transactional(readOnly = true)
	public Long extrairId(JwtAuthenticationToken token) {

		Long idToken = Long.parseLong(token.getName());

		if (userRepo.existsById(idToken)) {

			return idToken;
		} else
			throw new TokenInvalido("Usuario dono do token não foi encontrado");

	}

}
