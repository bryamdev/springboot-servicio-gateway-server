package com.micro.springboot.app.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SpringSecurityConfig {
	
	@Bean
	public SecurityWebFilterChain configure(ServerHttpSecurity http) {
		//Configura todas las rutas como protegidas y desactivar token de vista/formulario (csrf)
		return http.authorizeExchange()
				.pathMatchers("/api/v2/oauth/oauth/**").permitAll()
				.pathMatchers(HttpMethod.GET, 
						"/api/v2/productos/listar",
						"/api/v2/productos/ver/{id}",
						"/api/v2/items/listar",
						"/api/v2/items/ver/{id}/cantidad/{cantidad}",
						"/api/v2/usuarios/usuarios").permitAll()
				.pathMatchers(HttpMethod.GET, "/api/usuarios/usuarios/{id}").hasAnyRole("ADMIN", "USER")
				.pathMatchers("/api/v2/productos/**", 
						"/api/items/**", 
						"/api/usuarios/usuarios/**").hasRole("ADMIN")
				.anyExchange().authenticated()
				.and().csrf().disable()
				.build();
	}
	
	
	
}
