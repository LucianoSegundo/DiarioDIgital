package com.LSoftwareLTDA.diarioDigital.configuracoes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class FiltroSeguranca {

	@Bean
	public SecurityFilterChain segurancaFiltro(HttpSecurity http) throws Exception{
		String uriBasica = "/api/v1/usuario";
		String [] lista = {
				"/v3/api-docs.yaml",
				"/v3/api-docs/**",
				"/swagger-ui.html",
				"/swagger-ui/**"
				};
		http
		.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(HttpMethod.POST, uriBasica+"/").permitAll()
				.requestMatchers(HttpMethod.POST, uriBasica+"/login").permitAll()
				.requestMatchers(HttpMethod.PUT, uriBasica+"/recuperarSenha").permitAll()
				.requestMatchers(lista).permitAll()

				.anyRequest().authenticated()
				)
		.csrf(csrf -> csrf.disable())
		.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		return http.build();
	}
}
