package edu.icet.ecom.validation.annotations;

import edu.icet.ecom.validation.validators.AdultValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AdultValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAdult {
	String message () default "Age must be 18 or older";
	Class<?>[] groups () default {};
	Class<? extends Payload>[] payload () default {};
}
