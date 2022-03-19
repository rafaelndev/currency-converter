package com.jaya.challenge.currencyconverter.exception;

import com.jaya.challenge.currencyconverter.exception.error.RestError;
import com.jaya.challenge.currencyconverter.utils.DateUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
@Log4j2
public class RestExceptionHandler {

	@ExceptionHandler(ExchangeRatesApiException.class)
	public ResponseEntity<RestError> handleExchangeRatesApiException(ExchangeRatesApiException ex, ServerHttpRequest request) {
		RestError error = RestError.builder()
				.status(ex.getStatusCode())
				.errorCode(ex.getErrorCode())
				.message(ex.getMessage())
				.path(request.getPath().value())
				.timestamp(DateUtils.getCurrentDateUTC())
				.build();
		log.error(error);

		HttpStatus httpStatus = Optional.ofNullable(HttpStatus.resolve(ex.getStatusCode()))
				.orElse(HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(error, httpStatus);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<RestError> handleEntityNotFound(EntityNotFoundException ex, ServerHttpRequest request) {
		RestError error = RestError.builder()
				.status(HttpStatus.NOT_FOUND.value())
				.errorCode(HttpStatus.NOT_FOUND.getReasonPhrase())
				.message(ex.getMessage())
				.path(request.getPath().value())
				.timestamp(DateUtils.getCurrentDateUTC())
				.build();
		log.error(error);

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(WebExchangeBindException.class)
	public ResponseEntity<RestError> handleExchangeBindException(WebExchangeBindException ex, ServerHttpRequest request) {
		Map<String, String> validationErrors = ex.getFieldErrors().stream()
				.collect(Collectors.toMap(FieldError::getField, fieldError -> ObjectUtils.nullSafeToString(fieldError.getDefaultMessage())));

		RestError error = RestError.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.errorCode(HttpStatus.NOT_FOUND.getReasonPhrase())
				.path(request.getPath().value())
				.validationErrors(validationErrors)
				.timestamp(DateUtils.getCurrentDateUTC())
				.build();
		log.error(error);

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
}
