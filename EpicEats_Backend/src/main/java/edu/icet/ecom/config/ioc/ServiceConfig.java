package edu.icet.ecom.config.ioc;

import edu.icet.ecom.dto.User;
import edu.icet.ecom.entity.UserEntity;
import edu.icet.ecom.repository.CrudRepository;
import edu.icet.ecom.service.SuperServiceHandler;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ServiceConfig {
	private final CrudRepository<UserEntity> userEntityCrudRepository;
	private final ModelMapper mapper;

	@Bean
	public SuperServiceHandler<User, UserEntity> getAdminServiceHandler () {
		return new SuperServiceHandler<>(this.userEntityCrudRepository, this.mapper, User.class, UserEntity.class);
	}
}
