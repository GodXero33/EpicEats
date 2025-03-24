package edu.icet.ecom.config.apidoc.security.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(
	summary = "Delete an admin (todo)",
	description = "This endpoint delete an admin. No data return."
)
@ApiResponse(
	responseCode = "200",
	description = "Success - The admin was successfully deleted.",
	content = @Content(
		mediaType = "application/json",
		schemaProperties = {
			@SchemaProperty(name = "message", schema = @Schema(type = "string"))
		}
	)
)
@ApiResponse(
	responseCode = "304",
	description = "Invalid admin name - Failed to find admin with provided admin name.",
	content = @Content(
		mediaType = "application/json",
		schemaProperties = {
			@SchemaProperty(name = "message", schema = @Schema(type = "string"))
		}
	)
)
@ApiResponse(
	responseCode = "500",
	description = "Server error - An unexpected error occurred while processing the request. This typically occurs if there's a problem with the server or database.",
	content = @Content(
		mediaType = "application/json",
		schemaProperties = {
			@SchemaProperty(name = "message", schema = @Schema(type = "string"))
		}
	)
)
@ApiResponse(
	responseCode = "401",
	description = "Unauthorized - The provided Bearer token is either invalid, expired, or missing. Please ensure the token is valid and included in the request header.",
	content = @Content()
)
public @interface UserDeleteApiDoc {
}
