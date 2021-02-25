package com.pmlido.codechallenge.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static java.lang.String.*;

@RestController
@RequestMapping("/")
public class RootController {

	@Value("${spring.application.name}")
	private String appName;

	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public Mono<String> getApplicationInfo() {
		return Mono.just(format("{\"app\": \"%s\"}", appName));
	}
}


