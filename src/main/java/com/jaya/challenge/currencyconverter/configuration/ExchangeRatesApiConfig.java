package com.jaya.challenge.currencyconverter.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.config.exchange-rates-api")
@Getter
@Setter
public class ExchangeRatesApiConfig {
	private String url;
	private String accessKey;
}
