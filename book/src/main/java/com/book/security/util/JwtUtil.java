package com.book.security.util;

import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	// Use a consistent, Base64-encoded secret key
	private final String secretKey = Base64.getEncoder().encodeToString("mysecretkey".getBytes());

	public String generateToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours validity
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	public String extractUserName(String token) throws Exception {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws Exception {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public Claims extractAllClaims(String token) throws Exception {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		try {
			final String username = extractUserName(token);
			return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isTokenExpired(String token) throws Exception {
		final Date expiration = extractClaim(token, Claims::getExpiration);
		return expiration.before(new Date());
	}
}
