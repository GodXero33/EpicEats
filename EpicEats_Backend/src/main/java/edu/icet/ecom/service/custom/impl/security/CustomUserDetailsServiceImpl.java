package edu.icet.ecom.service.custom.impl.security;

import edu.icet.ecom.dto.security.User;
import edu.icet.ecom.dto.security.CustomUserDetails;
import edu.icet.ecom.entity.security.UserEntity;
import edu.icet.ecom.repository.custom.security.UserRepository;
import edu.icet.ecom.service.custom.security.CustomUserDetailsService;
import edu.icet.ecom.util.Response;
import edu.icet.ecom.util.enums.ResponseType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
	private final UserRepository userRepository;
	private final ModelMapper mapper;
	private final Logger logger;

	@Override
	public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
		final Response<UserEntity> response = this.userRepository.getByUserName(username);

		if (response.getStatus() == ResponseType.SERVER_ERROR) return null;

		if (response.getStatus() == ResponseType.NOT_FOUND) {
			this.logger.warn("Requested user not found: {}", username);
			return null;
		}

		return new CustomUserDetails(this.mapper.map(response.getData(), User.class));
	}
}
