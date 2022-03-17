package com.jaya.challenge.currencyconverter.data.repository;

import com.jaya.challenge.currencyconverter.data.domain.ConversionTransaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ConversionTransactionRepository extends ReactiveCrudRepository<ConversionTransaction, Integer> {
	Flux<ConversionTransaction> findByUserId(Integer userId);
}
