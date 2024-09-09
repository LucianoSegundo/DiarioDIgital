package com.LSoftwareLTDA.diarioDigital.configuracoes;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

@Configuration
public class JwtConfig {

	@Value("${jwt.publica.key}")
	private RSAPublicKey chavePu;
	
	@Value("${jwt.privada.key}")

	private RSAPrivateKey chavePri;
	
	@Bean
	public JwtDecoder decodificador() {
		return NimbusJwtDecoder
				.withPublicKey(chavePu)
				.build();
	}
	
	@Bean
	public JwtEncoder codificador() {
		JWK jwk = new RSAKey
				.Builder(chavePu)
				.privateKey(chavePri)
				.build();
	var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
	
	return new NimbusJwtEncoder(jwks);
	}
	
}
