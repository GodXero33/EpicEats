package edu.icet.ecom.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartUpConfig {
	private final ApplicationContext applicationContext;
	private final Logger logger;

	@EventListener(ApplicationReadyEvent.class)
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
