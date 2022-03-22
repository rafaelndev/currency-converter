package com.jaya.challenge.currencyconverter.resource.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Request body to convert endpoint")
public class ConversionRequest {

	@Schema(description = "Origin currency code for converting", example = "EUR")
	@NotEmpty
	private String originCurrency;

	@Schema(description = "Target currency code for converting", example = "BRL")
	@NotEmpty
	private String targetCurrency;

	@Schema(description = "Value to be converted", example = "5000")
	@NotNull
	@Positive
	private BigDecimal value;
}
