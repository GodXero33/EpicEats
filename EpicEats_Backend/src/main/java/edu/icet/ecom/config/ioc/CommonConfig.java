package edu.icet.ecom.config.ioc;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class CommonConfig {
	@Bean
	public ModelMapper getModelMapper () {
		return new ModelMapper();
	}

	@Bean
	public Logger getLogger () {
		return LoggerFactory.getLogger("GlobalLogger");
	}

	@Bean
	public BCryptPasswordEncoder getBCryptPasswordEncoder () {
		return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A, 12);
	}
}
