package edu.icet.ecom.service.custom.impl.security;

import edu.icet.ecom.service.custom.security.JWTService;
import edu.icet.ecom.util.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Primary
public class JWTServiceImpl implements JWTService {
	private final String secretKey;
	private final Integer tokenLifeTime;

	public JWTServiceImpl (
		@Value("${jwt.secret}") String secretKey,
		@Value("${jwt.lifetime}") Integer tokenLifeTime,
		@Value("${jwt.key_gen.enable}") boolean enableKeyGen
	) throws NoSuchAlgorithmException {
		this.tokenLifeTime = tokenLifeTime;

		if (enableKeyGen) {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
			SecretKey generatedSecretKey = keyGenerator.generateKey();
			this.secretKey = Base64.getUrlEncoder().encodeToString(generatedSecretKey.getEncoded());
		} else {
			this.secretKey = secretKey;
		}
	}

	@Override
	public String generateToken (String adminName, UserRole role) {
		final Map<String, Object> claims = new HashMap<>();

		claims.put("role", role);

		return Jwts.builder()
			.claims().add(claims)
			.subject(adminName)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + this.tokenLifeTime))
			.and()
			.signWith(this.getKey())
			.compact();
	}

	@Override
	public String extractUsername (String token) {
		return this.extractClaim(token, Claims::getSubject);
	}

	@Override
	public UserRole extractRole (String token) {
		return this.extractClaim(token, claims -> UserRole.fromName(claims.get("role", String.class)));
	}

	private <T> T extractClaim (String token, Function<Claims, T> claimResolver) {
		final Claims claims = this.extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	private Claims extractAllClaims (String token) {
		return Jwts.parser()
			.verifyWith(this.getKey())
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	private Date extractExpiration (String token) {
		return this.extractClaim(token, Claims::getExpiration);
	}

	private boolean isTokenExpired (String token) {
		return this.extractExpiration(token).before(new Date());
	}

	@Override
	public boolean validateToken (String token, UserDetails userDetails) {
		final String username = this.extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !this.isTokenExpired(token));
	}

	private SecretKey getKey () {
		final byte[] keyBytes = Decoders.BASE64URL.decode(this.secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
