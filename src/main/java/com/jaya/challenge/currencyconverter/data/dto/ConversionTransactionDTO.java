package com.jaya.challenge.currencyconverter.data.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ConversionTransactionDTO {
	private UUID transactionId;
	private String destinationCurrency;
	private BigDecimal destinationValue;
	private String originCurrency;
	private BigDecimal originValue;
	private BigDecimal exchangeRate;
	private ZonedDateTime createdDate;
	private Integer userId;
}
