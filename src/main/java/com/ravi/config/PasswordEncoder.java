package com.ravi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordEncoder {

	@Bean
	BCryptPasswordEncoder getEncodedPassword() {
		return new BCryptPasswordEncoder();
	}
}
