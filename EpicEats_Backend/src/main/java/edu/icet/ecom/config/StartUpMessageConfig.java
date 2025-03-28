package edu.icet.ecom.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartUpMessageConfig {
	private final ApplicationContext applicationContext;
	private final Logger logger;

	@PostConstruct
	public void onStartup () {
		this.logger.info("""
			
			 _____           _  __   __
			|  __ \\         | | \\ \\ / /
			| |  \\/ ___   __| |  \\ V /  ___ _ __ ___
			| | __ / _ \\ / _` |  /   \\ / _ \\ '__/ _ \\
			| |_\\ \\ (_) | (_| | / /^\\ \\  __/ | | (_) |
			 \\____/\\___/ \\__,_| \\/   \\/\\___|_|  \\___/
			""");

		if (this.applicationContext instanceof ConfigurableApplicationContext configurableApplicationContext) configurableApplicationContext.getBeanFactory().destroyBean(this);
	}
}
