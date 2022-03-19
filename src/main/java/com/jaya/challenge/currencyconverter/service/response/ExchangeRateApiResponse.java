package com.jaya.challenge.currencyconverter.service.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ExchangeRateApiResponse {
	private Boolean success;
	private Long timestamp;
	private String base;
	private String date;
	private Map<String, BigDecimal> rates;
}
