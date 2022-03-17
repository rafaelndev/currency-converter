package com.jaya.challenge.currencyconverter.resource;

import com.jaya.challenge.currencyconverter.resource.request.ConversionRequest;
import com.jaya.challenge.currencyconverter.resource.response.TransactionResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

public interface CurrencyConverterResource {
	@PostMapping
	Mono<TransactionResponse> convertCurrency(@RequestBody @Valid ConversionRequest request);
}
