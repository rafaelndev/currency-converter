package com.jaya.challenge.currencyconverter.data.repository;

import com.jaya.challenge.currencyconverter.data.domain.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Integer> {
}
