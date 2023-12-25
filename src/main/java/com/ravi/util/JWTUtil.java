package com.ravi.util;

import java.util.Base64;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

	@Value("${app.secret.key}")
	private String secretKey;
	
	// Code to generate Token.
	public String generateToken(String subject) {
		
		String tokenId = String.valueOf(new Random().nextInt(10000));
		String token = Jwts.builder()
			.setId(tokenId)
			.setSubject(subject)
			.setIssuer("ABC_LTD")
			.setAudience("XYZ_LTD")
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis()+TimeUnit.HOURS.toMillis(1)))
			.signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encode(secretKey.getBytes()))
			.compact();
		
		return token;
	}
	//Code to parse token.
	public Claims getClaims(String token) {
		
		Claims claims = Jwts.parser()
			.setSigningKey(Base64.getEncoder().encode(secretKey.getBytes()))
			.parseClaimsJws(token)
			.getBody();
		
		return claims;
	}
	
	//Code to check if token is valid.
	public boolean isValidToken(String token) {
		return getClaims(token).getExpiration().after(new Date(System.currentTimeMillis()));
	}
	
	//Code to check if token is valid as per userName.
	public boolean isValidToken(String token, String userName) {
		String tokenUserName = getSubject(token);
		return (userName.equals(tokenUserName)&& !isTokenExpired(token));
	}
	
	//Code to check if token is expired.
	public boolean isTokenExpired(String token) {
		return getExpirationDate(token).before(new Date(System.currentTimeMillis()));
	}
	
	//Code to get Expiration date.
	public Date getExpirationDate(String token) {
		return getClaims(token).getExpiration();
	}
	
	//Code to get subject.
	public String getSubject(String token) {
		return getClaims(token).getSubject();
	}
	
}
