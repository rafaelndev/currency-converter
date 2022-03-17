package com.jaya.challenge.currencyconverter.resource;

import com.jaya.challenge.currencyconverter.resource.request.ConversionRequest;
import com.jaya.challenge.currencyconverter.resource.response.TransactionResponse;
import com.jaya.challenge.currencyconverter.service.CurrencyConverterService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/currency-converter")
@AllArgsConstructor
public class CurrencyConverterController implements CurrencyConverterResource {
	private final CurrencyConverterService service;

	public Mono<TransactionResponse> convertCurrency(ConversionRequest request) {
		return service.convertCurrency(request.getUserId(), request.getTargetCurrency()).map(transaction -> {
			TransactionResponse transactionResponse = new TransactionResponse();

			transactionResponse.setTransactionId(transaction.getTransactionId().toString());
			transactionResponse.setDestinationCurrency(transaction.getDestinationCurrency());
			transactionResponse.setDestinationValue(transaction.getOriginValue().multiply(transaction.getExchangeRate()));
			transactionResponse.setOriginCurrency(transaction.getOriginCurrency());
			transactionResponse.setOriginValue(request.getValue());
			transactionResponse.setExchangeRate(transaction.getExchangeRate());
			transactionResponse.setCreatedDate(transaction.getCreatedDate());
			transactionResponse.setUserId(transaction.getUserId());
			return transactionResponse;
		});

	}
}
