package edu.icet.ecom.util.enumaration;

import java.util.Arrays;

public enum ResponseType {
	CREATED,
	NOT_CREATED,
	UPDATED,
	NOT_UPDATED,
	DELETED,
	NOT_DELETED,
	FOUND,
	NOT_FOUND,
	SUCCESS,
	FAILED,
	SERVER_ERROR;

	public static ResponseType fromName (String name) {
		return name == null ? null : Arrays.stream(ResponseType.values()).
			filter(responseType -> responseType.name().equalsIgnoreCase(name)).
			findFirst().
			orElse(null);
	}
}
