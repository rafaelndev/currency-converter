package com.jaya.challenge.currencyconverter.exception.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExchangeRateApiError {
	private String code;
	private String message;
}
