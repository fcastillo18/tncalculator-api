package com.tncalculator.tncalculatorapi.security.jwt;


import com.tncalculator.tncalculatorapi.model.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpirationMs}")
	private int jwtExpirationMs;


	public String generateJwtToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime expirationTime = currentTime.plusMinutes(jwtExpirationMs); // Set the expiration time (e.g., 30 minutes from the current time)

		Date expirationDate = Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant());


		return Jwts.builder()
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(expirationDate)
				.signWith((new SecretKeySpec(Base64.getDecoder().decode(jwtSecret),
						SignatureAlgorithm.HS256.getJcaName())))
				.compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(new SecretKeySpec(Base64.getDecoder().decode(jwtSecret),
						SignatureAlgorithm.HS256.getJcaName()))
				.build()
				.parseClaimsJws(token)
				.getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jws<Claims> claims = Jwts.parserBuilder()
					.setSigningKey((new SecretKeySpec(Base64.getDecoder().decode(jwtSecret),
							SignatureAlgorithm.HS256.getJcaName())))
					.build()
					.parseClaimsJws(authToken);

			Date expirationDate = claims.getBody().getExpiration();
			Date currentDate = new Date();
			return !currentDate.after(expirationDate); // Check if the current date is before the expiration date
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}