package edu.icet.ecom.dto.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
	private final User user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities () {
		return Collections.singleton(new SimpleGrantedAuthority(this.user.getRole().name()));
	}

	@Override
	public String getPassword () {
		return this.user.getPassword();
	}

	@Override
	public String getUsername () {
		return this.user.getUsername();
	}
}
