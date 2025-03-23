package edu.icet.ecom.validation.validators;

import edu.icet.ecom.validation.annotations.ValidAdult;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class AdultValidator implements ConstraintValidator<ValidAdult, String> {
	@Override
	public boolean isValid (String dob, ConstraintValidatorContext constraintValidatorContext) {
		if (dob == null || dob.isEmpty()) return false;

		try {
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			final LocalDate birthDate = LocalDate.parse(dob, formatter);
			final LocalDate today = LocalDate.now();

			return ChronoUnit.YEARS.between(birthDate, today) >= 18;
		} catch (Exception exception) {
			return false;
		}
	}
}
