package com.jaya.challenge.currencyconverter.service;

import com.jaya.challenge.currencyconverter.data.domain.ConversionTransaction;
import com.jaya.challenge.currencyconverter.data.repository.ConversionTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CurrencyConverterService {

	private final ConversionTransactionRepository repository;

	@Transactional
	public Mono<ConversionTransaction> convertCurrency(Integer userId, String targetCurrencyCode) {

		ConversionTransaction transaction = new ConversionTransaction();
		transaction.setTransactionId(UUID.randomUUID());
		transaction.setOriginCurrency("EUR");
		transaction.setExchangeRate(BigDecimal.valueOf(1.5));
		transaction.setDestinationCurrency(targetCurrencyCode);
		transaction.setOriginValue(BigDecimal.valueOf(500));
		transaction.setCreatedDate(getCurrentDateUTC());
		transaction.setUserId(userId);
		return repository.save(transaction);
	}

	private ZonedDateTime getCurrentDateUTC() {
		return ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));
	}
}
