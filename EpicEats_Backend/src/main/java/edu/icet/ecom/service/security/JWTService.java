package edu.icet.ecom.service.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
	String generateToken (String adminName);
	String extractUsername(String token);
	boolean validateToken (String token, UserDetails userDetails);
}
