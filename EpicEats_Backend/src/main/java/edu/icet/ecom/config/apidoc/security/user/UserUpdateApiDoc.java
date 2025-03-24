package edu.icet.ecom.config.apidoc.security.user;

import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(
	summary = "Update an admin (todo)",
	description = "This endpoint update an admin. It returns the updated admin details if update success."
)
public @interface UserUpdateApiDoc {
}
