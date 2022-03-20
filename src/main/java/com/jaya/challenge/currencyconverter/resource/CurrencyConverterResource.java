package com.jaya.challenge.currencyconverter.resource;

import com.jaya.challenge.currencyconverter.exception.error.RestError;
import com.jaya.challenge.currencyconverter.resource.request.ConversionRequest;
import com.jaya.challenge.currencyconverter.resource.response.TransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

public interface CurrencyConverterResource {
	@Operation(
			summary = "Convert a currency",
			description = "Convert a currency value based on current exchange rates"
	)
	@ApiResponse(description = "4xx Exchange Rates Errors and Api Errors", responseCode = "4xx", content = @Content(schema = @Schema(implementation = RestError.class)))
	@ApiResponse(responseCode = "200")
	@PostMapping(value = "/convert/{userId}")
	Mono<TransactionResponse> convertCurrency(@PathVariable(name = "userId") Integer userId, @RequestBody @Valid ConversionRequest request);

	@Operation(
			summary = "List user conversion transactions",
			description = "List all conversion transactions done by a user"
	)
	@ApiResponse(description = "4xx Exchange Rates Errors and Api Errors", responseCode = "4xx", content = @Content(schema = @Schema(implementation = RestError.class)))
	@ApiResponse(responseCode = "200")
	@GetMapping("transactions/{userId}")
	Flux<TransactionResponse> getUserTransactions(@PathVariable(name = "userId") Integer userId);
}
