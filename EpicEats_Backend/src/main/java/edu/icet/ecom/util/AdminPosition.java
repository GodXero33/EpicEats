package edu.icet.ecom.util;

import java.util.Arrays;

public enum AdminPosition {
	CASHIER, MANAGER;

	public static AdminPosition fromName (String name) {
		return name == null ? null : Arrays.stream(AdminPosition.values()).
			filter(adminPosition -> adminPosition.name().equalsIgnoreCase(name)).
			findFirst().
			orElse(null);
	}
}
