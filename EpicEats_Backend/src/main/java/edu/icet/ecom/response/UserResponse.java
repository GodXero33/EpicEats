package edu.icet.ecom.response;

import org.springframework.http.HttpStatusCode;

public class UserResponse<T> extends SuperResponse<T> {
	public UserResponse (HttpStatusCode httpStatusCode, T data, Object ...info) {
		super(httpStatusCode, data, info);
	}
}
