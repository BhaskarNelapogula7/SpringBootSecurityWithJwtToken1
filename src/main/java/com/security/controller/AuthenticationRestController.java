package com.security.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import org.springframework.security.core.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.exception.InvalidCredentialsException;
import com.security.exception.InvalidTokenException;
import com.security.model.Role;
import com.security.model.User;
import com.security.model.UserDetailsImpl;
import com.security.repository.UserRepository;
import com.security.request.LoginRequest;
import com.security.request.SignupRequest;
import com.security.response.JwtResponse;
import com.security.response.MessageResponse;
import com.security.util.JwtUtils;
import com.security.util.RolesUtils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;

@RestController
@RequestMapping("/auth")
public class AuthenticationRestController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RolesUtils rolesUtils;

	@Autowired
	private PasswordEncoder encoder;

	// register
	@PostMapping("/register")
	public ResponseEntity<?> createUser(@Valid @RequestBody SignupRequest signupRequest) {

		System.out.println("send request from post man" + signupRequest);
		// check username exist
		if (userRepository.existsByUsername(signupRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse(" Username already exist"));
		}
		// check email exist
		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse(" EmailId already exist"));
		}
		// create user
		User user = new User(signupRequest.getUsername(), signupRequest.getEmail(),
				encoder.encode(signupRequest.getPassword()));
		// roles given
		Set<String> usrRoles = signupRequest.getRole();
		// roles need to be stored in DB
		Set<Role> dbRoles = new HashSet<>();

		rolesUtils.mapRoles(usrRoles, dbRoles);
		user.setRoles(dbRoles);
		userRepository.save(user);
		System.out.println("this is the role getting from db"+user.getRoles());
		
		return new ResponseEntity<>(user, HttpStatus.OK);
		// return ResponseEntity.ok(new MessageResponse("User Created Successfully!"));
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			// check for Authentication
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			// set as SecurityContext(Authentication)
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// Generate JWT Token
			String jwt = jwtUtils.generateToken(authentication);
     
			
			// current user object
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

			// Create response headers and add the JWT token
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("Authorization", "Bearer " + jwt);
			responseHeaders.set("Username", userDetails.getUsername());
			responseHeaders.set("Email", userDetails.getEmail());

			// Create the JwtResponse object with the user details
			JwtResponse jwtResponse = new JwtResponse(jwt, // token
					userDetails.getId(), // id
					userDetails.getUsername(), // username
					userDetails.getEmail(), // email
					userDetails.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toSet()) // Set<String>
			);

			// Return response with the JwtResponse in the body and the JWT token in the
			// headers
			return ResponseEntity.ok().headers(responseHeaders).body(jwtResponse);
		} catch (AuthenticationException ex) {
			throw new InvalidCredentialsException("Invalid User Credentials");
		} catch (JwtException ex) {
			if (ex instanceof SignatureException) {
				throw new InvalidTokenException("Invalid Token Signature");
			} else {
				throw new InvalidTokenException("Invalid Token");
			}

		}
	}

}
