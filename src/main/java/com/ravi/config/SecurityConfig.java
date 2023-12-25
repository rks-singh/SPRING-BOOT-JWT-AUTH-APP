package com.ravi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ravi.filter.SecurityFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UnAuthorizedUserAuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	private SecurityFilter securityFilter;

	// Required in case of stateless Authentication.
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	return config.getAuthenticationManager();
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests()
            .requestMatchers("/saveUser", "/loginUser").permitAll()
            .anyRequest().authenticated()
            .and()
            .exceptionHandling(handling -> handling
            .authenticationEntryPoint(authenticationEntryPoint))
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
            .authenticationProvider(authenticationProvider());
		
		return http.build();
			
	}
	@Bean
	 AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
		return authenticationProvider;
	}
}
