package com.jaya.challenge.currencyconverter.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaya.challenge.currencyconverter.data.domain.ConversionTransaction;
import com.jaya.challenge.currencyconverter.data.domain.User;
import com.jaya.challenge.currencyconverter.data.repository.ConversionTransactionRepository;
import com.jaya.challenge.currencyconverter.data.repository.UserRepository;
import com.jaya.challenge.currencyconverter.exception.error.ExchangeRateApiResponseError;
import com.jaya.challenge.currencyconverter.generators.ConversionTransactionGenerator;
import com.jaya.challenge.currencyconverter.generators.ExchangeApiResponseGenerator;
import com.jaya.challenge.currencyconverter.resource.request.ConversionRequest;
import com.jaya.challenge.currencyconverter.service.response.ExchangeRateApiResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.test.StepVerifier;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "")
@AutoConfigureWebTestClient
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CurrencyConverterControllerTest {

	private static final String BASE_URL = "/api/v1/currency-converter";
	private static MockWebServer mockWebServer;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ConversionTransactionRepository transactionRepository;
	@Autowired
	private WebTestClient client;

	@BeforeAll
	static void beforeAll() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();

		Locale.setDefault(Locale.ENGLISH);
	}

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry r) {
		r.add("app.config.exchange-rates-api.url ", () -> String.format("http://localhost:%s", mockWebServer.getPort()));
		r.add("app.config.exchange-rates-api.accessKey ", () -> "access_key_test");
	}

	@AfterAll
	static void afterAll() throws IOException {
		mockWebServer.shutdown();
	}

	@Test
	@DisplayName("should convert currency and create a conversion transaction successfully")
	void shouldConvertCurrency() throws IOException {
		User user = addMockUser();
		String targetCurrency = "BRL";
		String originCurrency = "EUR";
		ConversionRequest request = getConversionRequest(originCurrency, targetCurrency, BigDecimal.valueOf(5000));
		ExchangeRateApiResponse expectedResponse = ExchangeApiResponseGenerator.getValid();

		mockWebServer.enqueue(new MockResponse()
				.setBody(new ObjectMapper().writeValueAsString(expectedResponse))
				.addHeader("Content-Type", "application/json")
		);

		postConvert(user.getId(), request)
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.transactionId").value((value) -> assertThat((String) value).hasSize(36))
				.jsonPath("$.destinationCurrency").isEqualTo(targetCurrency)
				.jsonPath("$.destinationValue").isEqualTo(7500.0)
				.jsonPath("$.originCurrency").isEqualTo("EUR")
				.jsonPath("$.originValue").isEqualTo(request.getValue())
				.jsonPath("$.exchangeRate").isEqualTo(1.5)
				.jsonPath("$.userId").isEqualTo(user.getId());
	}

	@Test
	@DisplayName("should fail to convert currency when exchange api returns error")
	void shouldFailToConvertWhenApiReturnsError() throws IOException {
		User user = addMockUser();
		String targetCurrency = "BRL";
		String originCurrency = "EUR";
		ConversionRequest request = getConversionRequest(originCurrency, targetCurrency, BigDecimal.valueOf(5000));
		ExchangeRateApiResponseError responseError = ExchangeApiResponseGenerator.getError();

		mockWebServer.enqueue(new MockResponse()
				.setResponseCode(400)
				.setBody(new ObjectMapper().writeValueAsString(responseError))
				.addHeader("Content-Type", "application/json")
		);

		postConvert(user.getId(), request)
				.expectStatus().isBadRequest()
				.expectBody()
				.jsonPath("$.status").isEqualTo(400)
				.jsonPath("$.errorCode").isEqualTo("test_error_code")
				.jsonPath("$.message").isEqualTo("test_error_message");
	}

	@Test
	@DisplayName("should fail to convert currency when user is invalid")
	void shouldFailToConvertCurrencyWithInvalidUser() {
		ConversionRequest request = getConversionRequest("EUR", "BRL", BigDecimal.valueOf(5000));
		postConvert(155, request)
				.expectStatus().isNotFound()
				.expectBody()
				.jsonPath("$.status").isEqualTo(404)
				.jsonPath("$.errorCode").isEqualTo("Not Found")
				.jsonPath("$.message").isEqualTo("User 155 not found");
	}

	@Test
	@DisplayName("should fail to convert currency when request is invalid with null parameters")
	void shouldFailToConvertCurrencyWithInvalidRequestNull() {
		ConversionRequest request = getConversionRequest(null, null, null);
		postConvert(20, request)
				.expectStatus().isBadRequest()
				.expectBody()
				.jsonPath("$.status").isEqualTo(400)
				.jsonPath("$.errorCode").isEqualTo("Bad Request")
				.jsonPath("$.validationErrors['targetCurrency']").isEqualTo("must not be empty")
				.jsonPath("$.validationErrors['originCurrency']").isEqualTo("must not be empty")
				.jsonPath("$.validationErrors['value']").isEqualTo("must not be null");
	}

	@Test
	@DisplayName("should fail to convert currency when request value is not positive")
	void shouldFailToConvertCurrencyWithNotPositiveValue() {
		ConversionRequest request = getConversionRequest(null, null, BigDecimal.valueOf(-5));
		postConvert(20, request)
				.expectStatus().isBadRequest()
				.expectBody()
				.jsonPath("$.status").isEqualTo(400)
				.jsonPath("$.errorCode").isEqualTo("Bad Request")
				.jsonPath("$.validationErrors['value']").isEqualTo("must be greater than 0");
	}

	@Test
	@DisplayName("should get a list of user transactions")
	void shouldGetAllUserTransactions() throws IOException {
		User user = addMockUser();

		List<ConversionTransaction> transactionList = ConversionTransactionGenerator.getValidList().stream()
				.map(conversionTransaction -> {
					conversionTransaction.setId(null);
					conversionTransaction.setUserId(user.getId());
					return conversionTransaction;
				}).collect(Collectors.toList());

		transactionRepository.saveAll(transactionList)
				.as(StepVerifier::create)
				.expectNextCount(2)
				.verifyComplete();

		getTransactions(user.getId())
				.expectStatus().isOk()
				.expectBody()
				.json(getJsonFromResource("json/currency-converter/response-get-user-transactions.json"));

	}

	@Test
	@DisplayName("should fail with 404 error when user id is invalid")
	void shouldFailWith404WhenUserIdIsInvalid() {
		getTransactions(155)
				.expectStatus().isNotFound()
				.expectBody()
				.jsonPath("$.status").isEqualTo(404)
				.jsonPath("$.errorCode").isEqualTo("Not Found")
				.jsonPath("$.message").isEqualTo("User 155 not found");

	}

	private User addMockUser() {
		User user = new User();
		user.setName("TestUser");

		return userRepository.save(user).block();
	}

	@NotNull
	private ConversionRequest getConversionRequest(String originCurrency, String targetCurrency, BigDecimal value) {
		ConversionRequest request = new ConversionRequest();
		request.setOriginCurrency(originCurrency);
		request.setTargetCurrency(targetCurrency);
		request.setValue(value);
		return request;
	}

	@NotNull
	private WebTestClient.ResponseSpec postConvert(int userId, ConversionRequest request) {
		return client.post()
				.uri(BASE_URL + "/convert/{userId}", userId)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(request))
				.exchange();
	}

	@NotNull
	private WebTestClient.ResponseSpec getTransactions(int userId) {
		return client.get()
				.uri(BASE_URL + "/transactions/{userId}", userId)
				.exchange();
	}

	private String getJsonFromResource(String path) throws IOException {
		File resource = new ClassPathResource(path).getFile();
		return new String(Files.readAllBytes(resource.toPath()));
	}

}