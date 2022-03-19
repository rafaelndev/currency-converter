package com.jaya.challenge.currencyconverter.service;

import com.jaya.challenge.currencyconverter.data.domain.ConversionTransaction;
import com.jaya.challenge.currencyconverter.data.domain.User;
import com.jaya.challenge.currencyconverter.data.repository.ConversionTransactionRepository;
import com.jaya.challenge.currencyconverter.data.repository.UserRepository;
import com.jaya.challenge.currencyconverter.exception.DataNotFoundException;
import com.jaya.challenge.currencyconverter.utils.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CurrencyConverterService {

	private final ConversionTransactionRepository repository;
	private final UserRepository userRepository;
	private final ExchangeRatesApiService exchangeRatesApiService;

	@Transactional
	public Mono<ConversionTransaction> convertCurrency(Integer userId, BigDecimal value, String targetCurrencyCode) {
		return getUserById(userId)
				.flatMap(user -> exchangeRatesApiService.getExchangeRate(targetCurrencyCode)
						.flatMap(exchangeRateApiResponse -> {
							ConversionTransaction transaction = new ConversionTransaction();
							transaction.setTransactionId(UUID.randomUUID());
							transaction.setOriginCurrency("EUR");
							transaction.setExchangeRate(exchangeRateApiResponse.getRates().get(targetCurrencyCode));
							transaction.setDestinationCurrency(targetCurrencyCode);
							transaction.setOriginValue(value);
							transaction.setCreatedDate(DateUtils.getCurrentDateUTC());
							transaction.setUserId(user.getId());
							return repository.save(transaction);
						}));

	}

	public Flux<ConversionTransaction> getTransactionsByUser(Integer userId) {
		return getUserById(userId)
				.flatMapMany(user -> repository.findByUserId(user.getId()));
	}


	private Mono<User> getUserById(Integer userId) {
		return userRepository.findById(userId)
				.switchIfEmpty(Mono.error(new DataNotFoundException(String.format("User %s not found", userId))));
	}

}
