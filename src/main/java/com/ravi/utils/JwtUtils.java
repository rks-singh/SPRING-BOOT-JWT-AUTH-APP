package com.ravi.utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {

	@Value("${app.secretKey}")
	private String secretKey;

	// Generating Token
	public String generateTocken(String Subject) {
		return Jwts.builder()
				.setSubject(Subject)
				.setIssuer("RaviSingh")
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15)))
				.signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
				.compact();
	}

	// Reading Claims
	public Claims getClaims(String token) {
		return Jwts.parser()
				.setSigningKey(secretKey.getBytes())
				.parseClaimsJws(token)
				.getBody();
	}

	// Getting Expire Date.
	public Date getExpDate(String token) {
		return getClaims(token).getExpiration();
	}
	
	// Getting userName
	public String getUserName(String token) {
		return getClaims(token).getSubject();
	}
	
	//Validate Expire Date.
	public boolean isTokenExp(String token) {
		Date expDate = getExpDate(token);
		return expDate.before(new Date(System.currentTimeMillis()));
	}
	
	//Validate userName in token and database, expDate
	public boolean validateToken(String token, String userName) {
		String tokenUserName = getUserName(token);
		return (userName.equals(tokenUserName)&& !isTokenExp(token));
		
	}
}
