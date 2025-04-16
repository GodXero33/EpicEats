package edu.icet.ecom.util.enums;

import java.util.Arrays;

public enum EmployeeRole {
	CASHIER, MANAGER, CHEF, WAITER, SUPERVISOR;

	public static EmployeeRole fromName (String name) {
		return name == null ? null : Arrays.stream(EmployeeRole.values()).
			filter(employeeRole -> employeeRole.name().equalsIgnoreCase(name)).
			findFirst().
			orElse(null);
	}
}
