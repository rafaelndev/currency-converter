package com.jaya.challenge.currencyconverter.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
		info = @Info(title = "Currency Converter", version = "0.1", description = "Documentation for Currency Converter API")
)
public class SpringDocConfiguration {
}
