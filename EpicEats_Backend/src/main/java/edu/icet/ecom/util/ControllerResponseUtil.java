package edu.icet.ecom.util;

import edu.icet.ecom.validation.ValidationErrorsHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
@RequiredArgsConstructor
public class ControllerResponseUtil {
	private final ValidationErrorsHelper validationErrorsHelper;

	public <T> CustomHttpResponse<T> getServerErrorResponse () {
		return new CustomHttpResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null, "Server error");
	}

	public <T> CustomHttpResponse<T> getInvalidDetailsResponse (String errorMessage) {
		return new CustomHttpResponse<>(HttpStatus.BAD_REQUEST, null, "Invalid details", errorMessage);
	}

	public <T> CustomHttpResponse<T> getInvalidDetailsResponse (BindingResult bindingResult) {
		return new CustomHttpResponse<>(HttpStatus.BAD_REQUEST, null, "Invalid details", this.validationErrorsHelper.getValidationErrors(bindingResult));
	}
}
