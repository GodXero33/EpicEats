package edu.icet.ecom.util.enumaration;

import java.util.Arrays;

public enum ReportType {
	DAILY, WEEKLY, MONTHLY, ANNUAL, CUSTOM;

	public static ReportType fromName (String name) {
		return name == null ? null : Arrays.stream(ReportType.values()).
			filter(reportType -> reportType.name().equalsIgnoreCase(name)).
			findFirst().
			orElse(null);
	}
}
