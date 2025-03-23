package edu.icet.ecom.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<T> {
	private final T data;
	private final ResponseType status;
}
