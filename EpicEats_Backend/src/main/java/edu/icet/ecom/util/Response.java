package edu.icet.ecom.util;

import edu.icet.ecom.util.enumaration.ResponseType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<T> {
	private final T data;
	private final ResponseType status;
}
