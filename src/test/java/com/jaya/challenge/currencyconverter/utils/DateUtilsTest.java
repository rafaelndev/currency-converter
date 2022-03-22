package com.jaya.challenge.currencyconverter.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

	@Test
	@DisplayName("should return a valid ZonedDateTime with UTC zone")
	void shouldReturnValidZonedDateTime() {
		final ZonedDateTime zonedDateTime = DateUtils.getCurrentDateUTC();

		assertThat(zonedDateTime)
				.isNotNull()
				.isBeforeOrEqualTo(ZonedDateTime.now());

		assertThat(zonedDateTime.getZone())
				.isEqualTo(ZoneId.of("UTC"));
	}

	@Test
	@DisplayName("should format a ZonedDateTime with UTC Format")
	void shouldFormatZonedDateTimeToUtc() {
		ZonedDateTime zonedDateTime = ZonedDateTime.of(2022, 5, 16, 10, 20, 56, 1234, ZoneId.of("UTC"));
		final String formattedZonedDateTime = DateUtils.formatZonedDateToUTC(zonedDateTime);

		assertThat(formattedZonedDateTime).isEqualTo("2022-05-16T10:20:56.000Z");
	}

}