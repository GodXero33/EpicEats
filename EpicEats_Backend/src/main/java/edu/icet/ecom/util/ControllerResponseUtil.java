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

	public <T> CustomHttpResponse<T> getServerErrorResponse (T data) {
		return new CustomHttpResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, data, "Server error");
	}

	public <T> CustomHttpResponse<T> getInvalidDetailsResponse (Object error) {
		return new CustomHttpResponse<>(HttpStatus.BAD_REQUEST, null, "Invalid user details", error instanceof BindingResult result ? this.validationErrorsHelper.getValidationErrors(result) : error);
	}
}
