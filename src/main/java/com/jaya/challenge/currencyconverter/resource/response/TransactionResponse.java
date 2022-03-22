package com.jaya.challenge.currencyconverter.resource.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionResponse {
	private String transactionId;
	private String destinationCurrency;
	private BigDecimal destinationValue;
	private String originCurrency;
	private BigDecimal originValue;
	private BigDecimal exchangeRate;
	private String createdDate;
	private Integer userId;
}
