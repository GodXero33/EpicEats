package edu.icet.ecom.config.apidoc.mail;

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
	summary = "Send an email",
	description = "This endpoint send email. Return an object with `message` property. If status code 200 email sent success. Other wise email sent failed."
)
@ApiResponse(
	responseCode = "200",
	description = "Success - The Email was successfully sent.",
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
public @interface SendEmailApiDoc {
}
