package edu.icet.ecom.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI customOpenAPI () {
		return new OpenAPI().info(
			new Info()
				.title("EpicEats API Documentation")
				.description("EpicEats is a food ordering and management system designed to provide a seamless experience for both customers and restaurant staff. This API enables features such as menu browsing, order placement, table reservations, and inventory management. It serves as the backend for the EpicEats web application, handling business logic, database interactions, and authentication.")
				.version("1.0.0")
				.contact(new Contact()
					.name("GodXero")
					.email("contact@godxero.dev.net")
					.url("https://www.godxero.dev.net"))
		).servers(List.of(
			new Server().url("http://localhost:8080").description("Local Server"),
			new Server().url("http://192.168.8.159:8080").description("Lan Server"),
			new Server().url("http://prepared-foxhound-prime.ngrok-free.app").description("Ngrok Server")
		));
	}
}
