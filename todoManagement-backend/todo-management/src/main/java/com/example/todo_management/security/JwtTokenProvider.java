package com.example.todo_management.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider 
{
	@Value("${app.jwt-secret}") //property key from appication.properties
	private String jwtSecret;
	
	@Value("${app.jwt-expiration-milliseconds}") 
	private long jwtExpirationDate;
	
	//Generate JWT token
	public String generateToken(Authentication authentication)
	{
		String usernameOrEmail = authentication.getName();
		
		Date currentDate = new Date();
		Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
		
		String token = Jwts.builder()
			.setSubject(usernameOrEmail)
			.setIssuedAt(new Date())
			.setExpiration(expireDate)
			.signWith(key()).compact();
		
		return token;
	}
	
	private Key key()
	{
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}
	
	//Get username from JWT token
	public String getUsername(String token)
	{
		Claims claims = Jwts.parser().setSigningKey(key()).build().parseClaimsJws(token).getBody();
		
		String username = claims.getSubject();
		
		return username;
	}
	
	//Validate JWT token
	public boolean validateToken(String token)
	{
		Jwts.parser().setSigningKey(key()).build().parse(token);
		
		return true;
	}
	
}
