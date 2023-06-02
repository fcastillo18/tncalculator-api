package com.tncalculator.tncalculatorapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO this is just for testing purposes, remove it later
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
//@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "5. Test", description = "This controller is just for testing purpose. The /api/test/all is open on purpose to check the endpoint functionality")
@Order(5)
public class TestController {
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@SecurityRequirement(name = "Bearer Authentication")
	public String userAccess() {
		return "User Content.";
	}


	@GetMapping("/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@SecurityRequirement(name = "Bearer Authentication")
	public String adminAccess() {
		return "Admin Board.";
	}
}