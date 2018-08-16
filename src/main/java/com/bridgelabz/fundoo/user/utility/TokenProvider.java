package com.bridgelabz.fundoo.user.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenProvider {

	//@Value("${jwt.key}")
	//private String key;
	private static final String KEY = "VIJAY";

	public String generateToken(String id) {

		return Jwts.builder().setId(id).signWith(SignatureAlgorithm.HS512, KEY).compact();
	}

	public String parseToken(String token) {
		Claims claim = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
		return claim.getId();
	}
}
