package com.LFSoftware.BroAvaliacao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class CodificadorSenha {
	
	@Bean
	public BCryptPasswordEncoder senhaCodificador() {
		return new BCryptPasswordEncoder();
	}
	
}