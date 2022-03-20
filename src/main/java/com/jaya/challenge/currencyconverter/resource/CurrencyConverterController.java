package com.jaya.challenge.currencyconverter.resource;

import com.jaya.challenge.currencyconverter.data.dto.ConversionTransactionDTO;
import com.jaya.challenge.currencyconverter.resource.request.ConversionRequest;
import com.jaya.challenge.currencyconverter.resource.response.TransactionResponse;
import com.jaya.challenge.currencyconverter.service.CurrencyConverterService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/currency-converter")
@AllArgsConstructor
@Log4j2
public class CurrencyConverterController implements CurrencyConverterResource {
	private final CurrencyConverterService service;

	public Mono<TransactionResponse> convertCurrency(Integer userId, ConversionRequest request) {
		log.info("Converting currency for user {}, with request: {}", userId, request);
		return service.convertCurrency(userId, request.getValue(), request.getTargetCurrency())
				.map(this::mapTransactionResponse);

	}

	@Override
	public Flux<TransactionResponse> getUserTransactions(Integer userId) {
		log.info("Obtaining user {} transactions", userId);
		return service.getTransactionsByUser(userId)
				.map(this::mapTransactionResponse);
	}

	private TransactionResponse mapTransactionResponse(ConversionTransactionDTO transaction) {
		TransactionResponse transactionResponse = new TransactionResponse();
		transactionResponse.setTransactionId(transaction.getTransactionId().toString());
		transactionResponse.setDestinationCurrency(transaction.getDestinationCurrency());
		transactionResponse.setDestinationValue(transaction.getDestinationValue());
		transactionResponse.setOriginCurrency(transaction.getOriginCurrency());
		transactionResponse.setOriginValue(transaction.getOriginValue());
		transactionResponse.setExchangeRate(transaction.getExchangeRate());
		transactionResponse.setCreatedDate(transaction.getCreatedDate());
		transactionResponse.setUserId(transaction.getUserId());
		return transactionResponse;
	}

}
