package edu.icet.ecom.response;

import org.springframework.http.HttpStatusCode;

public class MailResponse<T> extends SuperResponse<T> {
	public MailResponse (HttpStatusCode httpStatusCode, T data, Object ...info) {
		super(httpStatusCode, data, info);
	}
}
