package com.jaya.challenge.currencyconverter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaya.challenge.currencyconverter.configuration.ExchangeRatesApiConfig;
import com.jaya.challenge.currencyconverter.exception.ExchangeRatesApiException;
import com.jaya.challenge.currencyconverter.exception.error.ExchangeRatesApiError;
import com.jaya.challenge.currencyconverter.exception.error.ExchangeRatesApiResponseError;
import com.jaya.challenge.currencyconverter.generators.ExchangeApiResponseGenerator;
import com.jaya.challenge.currencyconverter.service.response.ExchangeRateApiResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class ExchangeRatesApiServiceTest {

	private static MockWebServer mockWebServer;
	private static ExchangeRatesApiService apiService;

	@BeforeAll
	static void beforeAll() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();

		ExchangeRatesApiConfig config = new ExchangeRatesApiConfig();
		config.setUrl(String.format("http://localhost:%s", mockWebServer.getPort()));
		config.setAccessKey("access_key_test");
		apiService = new ExchangeRatesApiService(config);
	}

	@AfterAll
	static void afterAll() throws IOException {
		mockWebServer.shutdown();
	}

	@Test
	@DisplayName("should return a valid ExchangeRateApiResponse on getExchangeRate")
	void shouldReturnValidResponseOnGetExchangeRate() throws JsonProcessingException, InterruptedException {
		String targetCurrency = "BRL";
		ExchangeRateApiResponse expectedResponse = ExchangeApiResponseGenerator.getValid();

		mockWebServer.enqueue(new MockResponse()
				.setBody(new ObjectMapper().writeValueAsString(expectedResponse))
				.addHeader("Content-Type", "application/json")
		);

		Mono<ExchangeRateApiResponse> exchangeRateResponse = apiService.getExchangeRate(targetCurrency);

		StepVerifier.create(exchangeRateResponse)
				.assertNext(exchangeRateApiResponse -> assertThat(exchangeRateApiResponse).usingRecursiveComparison().isEqualTo(expectedResponse))
				.verifyComplete();

		RecordedRequest recordedRequest = mockWebServer.takeRequest();
		assertThat(recordedRequest.getMethod()).isEqualTo("GET");
		assertThat(recordedRequest.getPath()).isEqualTo("/?access_key=access_key_test&base=EUR&symbols=BRL");

	}

	@Test
	@DisplayName("should return a ExchangeRatesApiException when request to api fails with error 4xx")
	void shouldReturnErrorWhenRequestFails() throws JsonProcessingException, InterruptedException {
		String targetCurrency = "BRL";
		ExchangeRatesApiError apiError = new ExchangeRatesApiError();
		apiError.setCode("test_error_code");
		apiError.setMessage("test_error_message");
		ExchangeRatesApiResponseError responseError = new ExchangeRatesApiResponseError();
		responseError.setError(apiError);

		mockWebServer.enqueue(new MockResponse()
				.setResponseCode(400)
				.setBody(new ObjectMapper().writeValueAsString(responseError))
				.addHeader("Content-Type", "application/json")
		);

		Mono<ExchangeRateApiResponse> exchangeRateResponse = apiService.getExchangeRate(targetCurrency);

		StepVerifier.create(exchangeRateResponse)
				.expectErrorSatisfies(throwable -> {
					assertThat(throwable).isInstanceOf(ExchangeRatesApiException.class);
					ExchangeRatesApiException exception = (ExchangeRatesApiException) throwable;
					assertThat(exception.getMessage()).isEqualTo("test_error_message");
					assertThat(exception.getStatusCode()).isEqualTo(400);
					assertThat(exception.getErrorCode()).isEqualTo("test_error_code");
				})
				.verify();
	}

}