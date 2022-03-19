package com.jaya.challenge.currencyconverter.resource;

import com.jaya.challenge.currencyconverter.data.domain.ConversionTransaction;
import com.jaya.challenge.currencyconverter.resource.request.ConversionRequest;
import com.jaya.challenge.currencyconverter.resource.response.TransactionResponse;
import com.jaya.challenge.currencyconverter.service.CurrencyConverterService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/currency-converter")
@AllArgsConstructor
public class CurrencyConverterController implements CurrencyConverterResource {
	private final CurrencyConverterService service;

	public Mono<TransactionResponse> convertCurrency(Integer userId, ConversionRequest request) {
		return service.convertCurrency(userId, request.getValue(), request.getTargetCurrency())
				.map(this::mapTransactionResponse);

	}

	@Override
	public Flux<TransactionResponse> getUserTransactions(Integer userId) {
		return service.getTransactionsByUser(userId)
				.map(this::mapTransactionResponse);
	}

	private TransactionResponse mapTransactionResponse(ConversionTransaction transaction) {
		TransactionResponse transactionResponse = new TransactionResponse();
		transactionResponse.setTransactionId(transaction.getTransactionId().toString());
		transactionResponse.setDestinationCurrency(transaction.getDestinationCurrency());
		transactionResponse.setDestinationValue(transaction.getOriginValue().multiply(transaction.getExchangeRate()));
		transactionResponse.setOriginCurrency(transaction.getOriginCurrency());
		transactionResponse.setOriginValue(transaction.getOriginValue());
		transactionResponse.setExchangeRate(transaction.getExchangeRate());
		transactionResponse.setCreatedDate(transaction.getCreatedDate());
		transactionResponse.setUserId(transaction.getUserId());
		return transactionResponse;
	}

}
