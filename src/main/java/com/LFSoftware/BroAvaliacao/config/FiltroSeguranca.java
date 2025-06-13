package com.LFSoftware.BroAvaliacao.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class FiltroSeguranca {

	@Value("${url.do.cliente}")
    private String origem;
	
	@Value("${cors.credencial}")
    private Boolean crosCredencial;
	
	@Bean
	public SecurityFilterChain segurancaFiltro(HttpSecurity http) throws Exception{
		String uriBasica = "/api/v1/usuario";
		String [] lista = {
				"/v3/api-docs.yaml",
				"/v3/api-docs/**",
				"/v3/api-docs",
				"/swagger-ui.html",
				"/swagger-ui/**",
				"/h2-console",
				"/h2-console/**"
				};
		http
		.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(HttpMethod.POST, uriBasica+"/").permitAll()
				.requestMatchers(HttpMethod.OPTIONS, uriBasica+"/").permitAll()
				.requestMatchers(HttpMethod.POST, uriBasica+"/login").permitAll()
				.requestMatchers(HttpMethod.OPTIONS, uriBasica+"/login").permitAll()
				.requestMatchers(lista).permitAll()
				.anyRequest().authenticated()
				)
		 .headers(headers -> headers
	                .frameOptions(frame -> frame.disable())
	            )
		.cors(Customizer.withDefaults())
		.csrf(csrf -> csrf.disable())
		.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		return http.build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		

		CorsConfiguration configuracao = new CorsConfiguration();
		configuracao.setAllowedOrigins(List.of(origem));
		configuracao.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuracao.setAllowedHeaders(List.of("*"));
		configuracao.setAllowCredentials(crosCredencial);
		
		UrlBasedCorsConfigurationSource fonte = new UrlBasedCorsConfigurationSource();
		fonte.registerCorsConfiguration("/**", configuracao);
		
		return fonte;
	}
}