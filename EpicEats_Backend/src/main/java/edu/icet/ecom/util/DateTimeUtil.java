package edu.icet.ecom.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
	private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private DateTimeUtil () {}

	public static LocalDate parseDate (String str) {
		return (str == null || str.isEmpty()) ? null : LocalDate.parse(str, DateTimeUtil.DATE_FORMATTER);
	}

	public static LocalTime parseTime (String str) {
		return (str == null || str.isEmpty()) ? null : LocalTime.parse(str, DateTimeUtil.TIME_FORMATTER);
	}

	public static LocalDateTime parseDateTime (String str) {
		return (str == null || str.isEmpty()) ? null : LocalDateTime.parse(str, DateTimeUtil.DATETIME_FORMATTER);
	}

	public static String getCurrentDate () {
		return LocalDate.now().format(DateTimeUtil.DATE_FORMATTER);
	}

	public static String getCurrentTime () {
		return LocalTime.now().format(DateTimeUtil.TIME_FORMATTER);
	}

	public static String getCurrentDateTime () {
		return LocalDateTime.now().format(DateTimeUtil.DATETIME_FORMATTER);
	}
}
