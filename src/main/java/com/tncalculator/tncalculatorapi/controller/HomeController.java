package com.tncalculator.tncalculatorapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeController {
	@GetMapping("/")
	public String home() {
		return "Hello to tncalculator-api";
	}

}