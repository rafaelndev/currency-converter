package com.jaya.challenge.currencyconverter.service;

import com.jaya.challenge.currencyconverter.data.domain.ConversionTransaction;
import com.jaya.challenge.currencyconverter.data.domain.User;
import com.jaya.challenge.currencyconverter.data.repository.ConversionTransactionRepository;
import com.jaya.challenge.currencyconverter.data.repository.UserRepository;
import com.jaya.challenge.currencyconverter.exception.EntityNotFoundException;
import com.jaya.challenge.currencyconverter.generators.ConversionTransactionGenerator;
import com.jaya.challenge.currencyconverter.generators.ExchangeApiResponseGenerator;
import com.jaya.challenge.currencyconverter.service.response.ExchangeRateApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CurrencyConverterServiceTest {
	@Mock
	private ConversionTransactionRepository repository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ExchangeRatesApiService exchangeRatesApiService;

	@InjectMocks
	private CurrencyConverterService service;

	@Test
	@DisplayName("should return a valid saved Conversion Transaction")
	void shouldReturnValidConversionTransaction() {
		final String targetCurrency = "BRL";
		final Integer userId = 10;
		final BigDecimal value = BigDecimal.valueOf(100);
		User user = new User();
		user.setId(10);
		ExchangeRateApiResponse apiResponse = ExchangeApiResponseGenerator.getValid();
		ConversionTransaction transaction = ConversionTransactionGenerator.getValid();

		Mockito.when(exchangeRatesApiService.getExchangeRate(targetCurrency)).thenReturn(Mono.just(apiResponse));
		Mockito.when(userRepository.findById(userId)).thenReturn(Mono.just(user));
		Mockito.when(repository.save(ArgumentMatchers.any(ConversionTransaction.class))).thenReturn(Mono.just(transaction));

		StepVerifier.create(service.convertCurrency(userId, value, targetCurrency))
				.expectSubscription()
				.assertNext(conversionTransaction -> assertThat(conversionTransaction).usingRecursiveComparison().isEqualTo(transaction))
				.verifyComplete();
	}

	@Test
	@DisplayName("should convert and save a successful transaction on convertCurrency")
	void shouldConvertAndSaveTransaction() {
		final String targetCurrency = "BRL";
		final Integer userId = 10;
		final BigDecimal value = BigDecimal.valueOf(100);
		User user = new User();
		user.setId(10);
		ExchangeRateApiResponse apiResponse = ExchangeApiResponseGenerator.getValid();

		Mockito.when(exchangeRatesApiService.getExchangeRate(targetCurrency)).thenReturn(Mono.just(apiResponse));
		Mockito.when(userRepository.findById(userId)).thenReturn(Mono.just(user));
		Mockito.when(repository.save(ArgumentMatchers.any(ConversionTransaction.class))).thenReturn(Mono.empty());

		StepVerifier.create(service.convertCurrency(userId, value, targetCurrency))
				.expectSubscription()
				.verifyComplete();

		ArgumentCaptor<ConversionTransaction> argumentCaptor = ArgumentCaptor.forClass(ConversionTransaction.class);
		Mockito.verify(repository).save(argumentCaptor.capture());
		ConversionTransaction capturedTransaction = argumentCaptor.getValue();

		assertThat(capturedTransaction.getTransactionId().toString()).hasSize(36);
		assertThat(capturedTransaction.getOriginCurrency()).isEqualTo("EUR");
		assertThat(capturedTransaction.getExchangeRate()).isEqualTo(BigDecimal.valueOf(1.5));
		assertThat(capturedTransaction.getDestinationCurrency()).isEqualTo(targetCurrency);
		assertThat(capturedTransaction.getOriginValue()).isEqualTo(value);
		assertThat(capturedTransaction.getCreatedDate()).isNotNull();
		assertThat(capturedTransaction.getUserId()).isEqualTo(userId);
	}

	@Test
	@DisplayName("should fail to convert currency when user is invalid")
	void shouldFailToConvertCurrency() {
		Integer userId = 10;

		Mockito.when(userRepository.findById(userId)).thenReturn(Mono.empty());

		StepVerifier.create(service.convertCurrency(userId, BigDecimal.valueOf(1.0), "BRL"))
				.expectSubscription()
				.expectErrorSatisfies(throwable -> {
					assertThat(throwable).isInstanceOf(EntityNotFoundException.class);
					EntityNotFoundException exception = (EntityNotFoundException) throwable;
					assertThat(exception.getMessage()).isEqualTo("User 10 not found");
				})
				.verify();
	}

	@Test
	@DisplayName("should return a flux of user transactions")
	void shouldReturnUserTransactions() {
		final Integer userId = 10;
		User user = new User();
		user.setId(10);
		Flux<ConversionTransaction> validList = ConversionTransactionGenerator.getValidFlux();

		Mockito.when(repository.findByUserId(userId)).thenReturn(validList);
		Mockito.when(userRepository.findById(userId)).thenReturn(Mono.just(user));

		StepVerifier.create(service.getTransactionsByUser(userId))
				.expectSubscription()
				.expectNextSequence(Objects.requireNonNull(validList.collectList().block()))
				.verifyComplete();
	}

	@Test
	@DisplayName("should fail to return user transactions when user is invalid")
	void shouldFailToReturnUserTransactions() {
		final Integer userId = 10;

		Mockito.when(userRepository.findById(userId)).thenReturn(Mono.empty());

		StepVerifier.create(service.getTransactionsByUser(userId))
				.expectSubscription()
				.expectErrorSatisfies(throwable -> {
					assertThat(throwable).isInstanceOf(EntityNotFoundException.class);
					EntityNotFoundException exception = (EntityNotFoundException) throwable;
					assertThat(exception.getMessage()).isEqualTo("User 10 not found");
				})
				.verify();
	}


}