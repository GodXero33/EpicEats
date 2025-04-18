package edu.icet.ecom.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class StartUpConfig {
	private final ApplicationContext applicationContext;
	private final Logger logger;
	@Value("${jwt.keygen.enable:false}")
	private boolean enableKeyGen;

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

	@PostConstruct
	public void generateJWTKey () throws NoSuchAlgorithmException {
		if (!this.enableKeyGen) return;

		final KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
		final SecretKey generatedSecretKey = keyGenerator.generateKey();
		final String secretKey = Base64.getUrlEncoder().encodeToString(generatedSecretKey.getEncoded());
		System.out.println(secretKey);
	}
}
