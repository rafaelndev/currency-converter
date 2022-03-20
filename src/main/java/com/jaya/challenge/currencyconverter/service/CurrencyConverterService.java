package com.jaya.challenge.currencyconverter.service;

import com.jaya.challenge.currencyconverter.data.domain.ConversionTransaction;
import com.jaya.challenge.currencyconverter.data.domain.User;
import com.jaya.challenge.currencyconverter.data.dto.ConversionTransactionDTO;
import com.jaya.challenge.currencyconverter.data.repository.ConversionTransactionRepository;
import com.jaya.challenge.currencyconverter.data.repository.UserRepository;
import com.jaya.challenge.currencyconverter.exception.EntityNotFoundException;
import com.jaya.challenge.currencyconverter.service.response.ExchangeRateApiResponse;
import com.jaya.challenge.currencyconverter.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class CurrencyConverterService {

	private final ConversionTransactionRepository repository;
	private final UserRepository userRepository;
	private final ExchangeRatesApiService exchangeRatesApiService;

	@Transactional
	public Mono<ConversionTransactionDTO> convertCurrency(Integer userId, BigDecimal value, String targetCurrencyCode) {
		return getUserById(userId)
				.flatMap(user -> exchangeRatesApiService.getExchangeRate(targetCurrencyCode)
						.flatMap(exchangeRateApiResponse -> {
							log.info("Received exchange api response: {}", exchangeRateApiResponse);
							ConversionTransaction transaction = mapConversionTransaction(value, targetCurrencyCode, user, exchangeRateApiResponse);
							BigDecimal calculatedValue = calculateDestinationValue(transaction);
							return mapTransactionToDTO(repository.save(transaction), calculatedValue);
						}));

	}

	public Flux<ConversionTransactionDTO> getTransactionsByUser(Integer userId) {
		return getUserById(userId)
				.flatMapMany(user -> repository.findByUserId(user.getId())
						.flatMap(conversionTransaction -> {
							BigDecimal calculatedValue = calculateDestinationValue(conversionTransaction);
							return mapTransactionToDTO(Mono.just(conversionTransaction), calculatedValue);
						}));
	}

	private ConversionTransaction mapConversionTransaction(BigDecimal value, String targetCurrencyCode, User user, ExchangeRateApiResponse exchangeRateApiResponse) {
		ConversionTransaction transaction = new ConversionTransaction();
		transaction.setTransactionId(UUID.randomUUID());
		transaction.setOriginCurrency("EUR");
		transaction.setExchangeRate(exchangeRateApiResponse.getRates().get(targetCurrencyCode));
		transaction.setDestinationCurrency(targetCurrencyCode);
		transaction.setOriginValue(value);
		transaction.setCreatedDate(DateUtils.getCurrentDateUTC());
		transaction.setUserId(user.getId());
		return transaction;
	}

	private BigDecimal calculateDestinationValue(ConversionTransaction conversionTransaction) {
		return conversionTransaction.getOriginValue().multiply(conversionTransaction.getExchangeRate());
	}

	private Mono<ConversionTransactionDTO> mapTransactionToDTO(Mono<ConversionTransaction> conversionTransaction, BigDecimal destinationValue) {
		return conversionTransaction.flatMap(transaction -> {
			ConversionTransactionDTO dto = new ConversionTransactionDTO();
			BeanUtils.copyProperties(transaction, dto, "id");
			dto.setDestinationValue(destinationValue);
			return Mono.just(dto);
		});
	}


	private Mono<User> getUserById(Integer userId) {
		return userRepository.findById(userId)
				.switchIfEmpty(Mono.error(new EntityNotFoundException(String.format("User %s not found", userId))));
	}

}
