package com.jaya.challenge.currencyconverter.exception;

import lombok.Getter;

@Getter
public class ExchangeRatesApiException extends Exception {
	private final Integer statusCode;
	private final String errorCode;

	public ExchangeRatesApiException(Integer statusCode, String errorCode, String errorMessage) {
		super(errorMessage);
		this.errorCode = errorCode;
		this.statusCode = statusCode;
	}
}
