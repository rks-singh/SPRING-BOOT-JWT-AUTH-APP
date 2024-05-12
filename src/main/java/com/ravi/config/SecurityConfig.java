package com.ravi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private InvalidUserAuthEntryPoint authenticationEntryPoint;
	
	@Autowired
	private SecurityFilter securityFilter;
	
	// For StateLess AUTH
	@Bean
	protected AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
			return config.getAuthenticationManager();
	}

	@Autowired
	public void configureUser(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}


	@Bean
	SecurityFilterChain configureSecurity(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((request) -> {
                    try {
                        request
                                .requestMatchers("user/save", "user/login").permitAll()
                                .anyRequest().authenticated()
                                .and()
                                .exceptionHandling(handling -> handling.authenticationEntryPoint(authenticationEntryPoint))
                                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                .and()
                                //register filter from 2nd request.
                                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class));
                    } catch (Exception e) { 
                        e.printStackTrace();
                    }
                });
		return http.build();
	}

}
