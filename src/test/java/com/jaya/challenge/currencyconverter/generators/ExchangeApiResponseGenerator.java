package com.jaya.challenge.currencyconverter.generators;

import com.jaya.challenge.currencyconverter.exception.error.ExchangeRateApiError;
import com.jaya.challenge.currencyconverter.exception.error.ExchangeRateApiResponseError;
import com.jaya.challenge.currencyconverter.service.response.ExchangeRateApiResponse;

import java.math.BigDecimal;
import java.util.Map;

public class ExchangeApiResponseGenerator {

	private ExchangeApiResponseGenerator() {
	}

	public static ExchangeRateApiResponse getValid() {
		ExchangeRateApiResponse response = new ExchangeRateApiResponse();
		response.setSuccess(true);
		response.setTimestamp(1647704826L);
		response.setBase("EUR");
		response.setDate("2020-03-19");
		response.setRates(Map.of(
				"BRL", BigDecimal.valueOf(1.5)
		));

		return response;
	}

	public static ExchangeRateApiResponseError getError() {
		ExchangeRateApiError apiError = new ExchangeRateApiError();
		apiError.setCode("test_error_code");
		apiError.setMessage("test_error_message");
		ExchangeRateApiResponseError responseError = new ExchangeRateApiResponseError();
		responseError.setError(apiError);

		return responseError;
	}
}
