package com.jaya.challenge.currencyconverter.resource.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ConversionRequest {
	@NotEmpty
	private String targetCurrency;

	@NotNull
	@Positive
	private BigDecimal value;
}
