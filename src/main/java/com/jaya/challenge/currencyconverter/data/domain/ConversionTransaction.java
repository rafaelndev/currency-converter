package com.jaya.challenge.currencyconverter.data.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Getter
@Setter
@Table("conversion_transaction")
public class ConversionTransaction {
	@Id
	private Integer id;
	@NotNull
	private UUID transactionId;
	@NotNull
	private String destinationCurrency;
	@NotNull
	private String originCurrency;
	@NotNull
	private BigDecimal originValue;
	@NotNull
	private BigDecimal exchangeRate;
	@NotNull
	private ZonedDateTime createdDate;
	@NotNull
	private Integer userId;
}
