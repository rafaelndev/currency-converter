package com.jaya.challenge.currencyconverter.exception.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestError {
	private ZonedDateTime timestamp;
	private Integer status;
	private String errorCode;
	private String message;
	private Map<String, String> validationErrors;
	private String path;
}
