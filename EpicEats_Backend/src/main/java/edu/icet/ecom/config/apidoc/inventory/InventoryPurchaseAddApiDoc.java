package edu.icet.ecom.config.apidoc.inventory;

import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(
	summary = "(todo)",
	description = "(todo)"
)
public @interface InventoryPurchaseAddApiDoc {
}
