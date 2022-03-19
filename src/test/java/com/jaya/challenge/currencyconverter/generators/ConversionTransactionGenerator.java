package com.jaya.challenge.currencyconverter.generators;

import com.jaya.challenge.currencyconverter.data.domain.ConversionTransaction;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public class ConversionTransactionGenerator {

	private ConversionTransactionGenerator() {
	}

	public static Flux<ConversionTransaction> getValidList() {
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
		return Flux.just(transaction1, transaction2);
	}

	public static ConversionTransaction getValid() {
		ConversionTransaction transaction1 = new ConversionTransaction();
		transaction1.setId(1);
		transaction1.setTransactionId(UUID.fromString("efed3324-9278-41a2-be92-972884f94207"));
		transaction1.setDestinationCurrency("BRL");
		transaction1.setOriginCurrency("EUR");
		transaction1.setOriginValue(BigDecimal.valueOf(5000));
		transaction1.setExchangeRate(BigDecimal.valueOf(3.77));
		transaction1.setCreatedDate(ZonedDateTime.parse("2022-03-19T15:19:07.7138024Z"));
		return transaction1;
	}

}
