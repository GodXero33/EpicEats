package edu.icet.ecom.service.custom.security;

import edu.icet.ecom.util.enums.UserRole;
import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
	String generateToken (String adminName, UserRole role);
	String extractUsername (String token);
	UserRole extractRole (String token);
	boolean validateToken (String token, UserDetails userDetails);
}
