package com.ravi.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ravi.util.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired(required=true)
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//Reading Token from Authorization header.
		String token = request.getHeader("Authorization");
		if(token!=null) {
			String userName = jwtUtil.getSubject(token);
			
			// if userName is not null and context Authentication must be null.
			if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
				
				UserDetails user = userDetailsService.loadUserByUsername(userName);
				boolean isValid = jwtUtil.isValidToken(token,user.getUsername());
				if(isValid) {
					UsernamePasswordAuthenticationToken authTocken = 
							new UsernamePasswordAuthenticationToken(userName, user.getUsername(),user.getAuthorities());
					authTocken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authTocken);
				}
			}
		}
		filterChain.doFilter(request, response);
		
	}
	
	
}
