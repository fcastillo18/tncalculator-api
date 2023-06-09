package com.tncalculator.tncalculatorapi.controller;

import com.tncalculator.tncalculatorapi.constant.ERole;
import com.tncalculator.tncalculatorapi.constant.EUserStatus;
import com.tncalculator.tncalculatorapi.exception.CustomException;
import com.tncalculator.tncalculatorapi.model.Role;
import com.tncalculator.tncalculatorapi.model.User;
import com.tncalculator.tncalculatorapi.model.UserDetailsImpl;
import com.tncalculator.tncalculatorapi.payload.request.LoginRequest;
import com.tncalculator.tncalculatorapi.payload.request.SignupRequest;
import com.tncalculator.tncalculatorapi.payload.response.JwtResponse;
import com.tncalculator.tncalculatorapi.payload.response.MessageResponse;
import com.tncalculator.tncalculatorapi.repository.RoleRepository;
import com.tncalculator.tncalculatorapi.repository.UserRepository;
import com.tncalculator.tncalculatorapi.security.jwt.JwtUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.tncalculator.tncalculatorapi.constant.Constant.INITIAL_BALANCE;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "1. Authentication", description = "The Authentication API, provides the endpoints for authentication")
@Order(1)
public class AuthController {
	private final AuthenticationManager authenticationManager;

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final PasswordEncoder encoder;

	private final JwtUtils jwtUtils;

	@Autowired
	public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
						  RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.encoder = encoder;
		this.jwtUtils = jwtUtils;
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			// Get the authenticated user details
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

			// Check if the user is active
			User user = userRepository.findById(userDetails.getId()).orElseThrow();
			if (!user.getStatus().equalsIgnoreCase(("active"))) {
				throw new CustomException("User is inactive");
			}

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			List<String> roles = userDetails.getAuthorities().stream()
					.map(GrantedAuthority::getAuthority)
					.collect(Collectors.toList());

			return ResponseEntity.ok(new JwtResponse(jwt,
					user,
					roles));
		} catch (AuthenticationException e) {
			// Handle incorrect username or password
			throw new CustomException("Incorrect username or password");
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) { // Signup entity is missing User fields
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		User user = User.builder()
				.username(signUpRequest.getUsername())
				.email(signUpRequest.getEmail())
				.password(encoder.encode(signUpRequest.getPassword()))
				.status(EUserStatus.ACTIVE.getStatus())
				.balance(signUpRequest.getBalance()  == null ? INITIAL_BALANCE : signUpRequest.getBalance())
				.build();

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				if (role.equals("admin")) {
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
				} else {
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}