package com.jaya.challenge.currencyconverter.generators;

import com.jaya.challenge.currencyconverter.data.domain.ConversionTransaction;
import com.jaya.challenge.currencyconverter.data.dto.ConversionTransactionDTO;
import org.springframework.beans.BeanUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ConversionTransactionGenerator {

	private ConversionTransactionGenerator() {
	}

	public static Flux<ConversionTransaction> getValidFlux() {
		Mono<List<ConversionTransaction>> list = Mono.just(getValidList());
		return list.flatMapMany(Flux::fromIterable);
	}

	public static List<ConversionTransaction> getValidList() {
		ConversionTransaction transaction1 = new ConversionTransaction();
		transaction1.setId(1);
		transaction1.setTransactionId(UUID.fromString("efed3324-9278-41a2-be92-972884f94207"));
		transaction1.setDestinationCurrency("BRL");
		transaction1.setOriginCurrency("EUR");
		transaction1.setOriginValue(BigDecimal.valueOf(5000));
		transaction1.setExchangeRate(BigDecimal.valueOf(3.77));
		transaction1.setCreatedDate(ZonedDateTime.parse("2022-03-19T15:19:07.7138024Z"));
		transaction1.setUserId(10);

		ConversionTransaction transaction2 = new ConversionTransaction();
		transaction2.setId(2);
		transaction2.setTransactionId(UUID.fromString("cb13f192-6adb-451e-9cee-7985f9695b2a"));
		transaction2.setDestinationCurrency("USD");
		transaction2.setOriginCurrency("EUR");
		transaction2.setOriginValue(BigDecimal.valueOf(4500));
		transaction2.setExchangeRate(BigDecimal.valueOf(4.99));
		transaction2.setCreatedDate(ZonedDateTime.parse("2022-03-18T13:12:01.2336025Z"));
		transaction2.setUserId(10);
		return new ArrayList<>(List.of(transaction1, transaction2));
	}

	public static List<ConversionTransactionDTO> getValidDTOList() {
		return getValidList().stream().map(conversionTransaction -> {
			ConversionTransactionDTO dto = new ConversionTransactionDTO();
			BeanUtils.copyProperties(conversionTransaction, dto, "id");
			dto.setDestinationValue(conversionTransaction.getOriginValue().multiply(conversionTransaction.getExchangeRate()));
			return dto;
		}).collect(Collectors.toList());
	}

	public static ConversionTransactionDTO getValidDTO(BigDecimal originValue, BigDecimal exchangeRate) {
		ConversionTransaction valid = getValid(originValue, exchangeRate);
		ConversionTransactionDTO dto = new ConversionTransactionDTO();
		BeanUtils.copyProperties(valid, dto, "id");
		dto.setDestinationValue(valid.getOriginValue().multiply(exchangeRate));
		return dto;
	}

	public static ConversionTransaction getValid(BigDecimal originValue, BigDecimal exchangeRate) {
		ConversionTransaction transaction1 = new ConversionTransaction();
		transaction1.setId(1);
		transaction1.setTransactionId(UUID.fromString("efed3324-9278-41a2-be92-972884f94207"));
		transaction1.setDestinationCurrency("BRL");
		transaction1.setOriginCurrency("EUR");
		transaction1.setOriginValue(originValue);
		transaction1.setExchangeRate(exchangeRate);
		transaction1.setCreatedDate(ZonedDateTime.parse("2022-03-19T15:19:07.7138024Z"));
		transaction1.setUserId(1);
		return transaction1;
	}

}
