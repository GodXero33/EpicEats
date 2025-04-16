package edu.icet.ecom.util.enums;

import java.util.Arrays;

public enum ExpenseType {
	RENT, SALARIES, UTILITIES, MAINTENANCE, OTHER;

	public static ExpenseType fromName (String name) {
		return name == null ? null : Arrays.stream(ExpenseType.values()).
			filter(expenseType -> expenseType.name().equalsIgnoreCase(name)).
			findFirst().
			orElse(null);
	}
}
