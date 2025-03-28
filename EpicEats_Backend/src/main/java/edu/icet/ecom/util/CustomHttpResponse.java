package edu.icet.ecom.util;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class CustomHttpResponse<T> extends ResponseEntity<Map<String, Object>> {
	private static final String[] INFO_KEYS = { "message", "error" };

	public CustomHttpResponse (HttpStatusCode httpStatusCode, T data, Object ...info) {
		super(CustomHttpResponse.getResponse(data, info), httpStatusCode);
	}

	private static <T> Map<String, Object> getResponse (T data, Object[] info) {
		final Map<String, Object> response = new HashMap<>();

		if (data != null) response.put("data", data);

		for (int a = 0; a < info.length; a++) response.put(CustomHttpResponse.INFO_KEYS[a], info[a]);

		return response;
	}
}
