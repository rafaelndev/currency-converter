package com.jaya.challenge.currencyconverter.service;

import com.jaya.challenge.currencyconverter.configuration.ExchangeRatesApiConfig;
import com.jaya.challenge.currencyconverter.exception.ExchangeRatesApiException;
import com.jaya.challenge.currencyconverter.exception.error.ExchangeRatesApiResponseError;
import com.jaya.challenge.currencyconverter.service.response.ExchangeRateApiResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@Log4j2
public class ExchangeRatesApiService {
	private static final String DEFAULT_SOURCE_CURRENCY = "EUR";
	private final WebClient client;
	private final ExchangeRatesApiConfig config;

	@Autowired
	public ExchangeRatesApiService(ExchangeRatesApiConfig config) {
		client = WebClient.builder()
				.build();
		this.config = config;
	}

	public Mono<ExchangeRateApiResponse> getExchangeRate(String targetCurrency) {
		log.info("Trying to Get Exchange Rate, URL: {}, targetCurrency: {}", config.getUrl(), targetCurrency);
		return client.get()
				.uri(config.getUrl(),
						uri -> uri.queryParam("access_key", config.getAccessKey())
								.queryParam("base", DEFAULT_SOURCE_CURRENCY)
								.queryParam("symbols", targetCurrency)
								.build())
				.retrieve()
				.onStatus(HttpStatus::is4xxClientError, handle4xxApiError())
				.onStatus(HttpStatus::is5xxServerError, handle5xxApiError())
				.bodyToMono(ExchangeRateApiResponse.class);
	}

	private Function<ClientResponse, Mono<? extends Throwable>> handle4xxApiError() {
		return response -> response.bodyToMono(ExchangeRatesApiResponseError.class)
				.map(ExchangeRatesApiResponseError::getError)
				.flatMap(error -> Mono.error(
						new ExchangeRatesApiException(response.statusCode().value(), error.getCode(), error.getMessage())
				));
	}

	private Function<ClientResponse, Mono<? extends Throwable>> handle5xxApiError() {
		return response -> response.bodyToMono(String.class)
				.flatMap(errorBody -> {
					log.error("Exchange Rates Api Internal Error [{}]: {}", response.statusCode().value(), errorBody);
					return Mono.error(
							new ExchangeRatesApiException(response.statusCode().value(), "Internal Error", "Exchange Rates Api Internal " +
									"Error"));
				});
	}
}
