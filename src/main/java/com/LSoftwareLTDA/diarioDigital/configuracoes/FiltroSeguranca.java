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
	public SecurityFilterChain filtroSeguranca(HttpSecurity http) throws Exception{
		String uriBasica = "/api/v1/usuario";
		http
		.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(HttpMethod.POST, uriBasica+"/cadastro").permitAll()
				.requestMatchers(HttpMethod.POST, uriBasica+"/login").permitAll()
				.anyRequest().authenticated()
				)
		.csrf(csrf -> csrf.disable())
		.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		return http.build();
	}
}
