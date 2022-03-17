package com.jaya.challenge.currencyconverter.resource.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ConversionRequest {
	@NotNull
	private Integer userId;

	@NotEmpty
	private String targetCurrency;

	@NotNull
	private BigDecimal value;
}
