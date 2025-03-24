package edu.icet.ecom.config.apidoc.security.user;

import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(
	summary = "Register a new admin (todo)",
	description = "This endpoint registers a new admin. It returns the admin data if successful, or an error message if the registration fails. The admin data will contain details like name, email, and phone number, while the response messages provide detailed status on success or failure."
)
public @interface UserRegisterApiDoc {
}
