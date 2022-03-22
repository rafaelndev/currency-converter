package com.jaya.challenge.currencyconverter.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
	private static final String UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";

	private DateUtils() {
	}

	public static ZonedDateTime getCurrentDateUTC() {
		return ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));
	}

	public static String formatZonedDateToUTC(ZonedDateTime zonedDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtils.UTC_FORMAT);
		return zonedDateTime.format(formatter);
	}

}
