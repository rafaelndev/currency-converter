package com.jaya.challenge.currencyconverter.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateUtils {

	private DateUtils() {
	}

	public static ZonedDateTime getCurrentDateUTC() {
		return ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));
	}

}
