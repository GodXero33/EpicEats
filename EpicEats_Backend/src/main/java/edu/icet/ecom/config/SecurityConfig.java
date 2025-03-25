package edu.icet.ecom.config;

import edu.icet.ecom.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final UserDetailsService userDetailsService;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtFilter jwtFilter;

	@Bean
	public SecurityFilterChain getSecurityFilterChain (HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(request -> request
				.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
				.requestMatchers("user/register", "user/login").permitAll()
				.anyRequest().authenticated())
			.formLogin(Customizer.withDefaults())
			.httpBasic(Customizer.withDefaults())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
			.addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
	}

	@Bean
	public AuthenticationProvider getAuthenticationProvider () {
		final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

		provider.setPasswordEncoder(this.passwordEncoder);
		provider.setUserDetailsService(this.userDetailsService);

		return provider;
	}

	@Bean
	public AuthenticationManager getAuthenticationManager (AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
