package com.jaya.challenge.currencyconverter.data.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@Table("user")
public class User {
	@Id
	private Integer id;
	@NotNull
	private String name;
}
