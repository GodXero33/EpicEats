package edu.icet.ecom.config.apidoc.security.user;

import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(
	summary = "Admin login (todo)",
	description = "This endpoint login a admin. It returns the login Bearer token after login details authorized."
)
public @interface UserLoginApiDoc {
}
