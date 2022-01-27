package com.wetark.main.controller;

import com.wetark.main.helper.rocketChat.payload.response.RCLoginResponse;
import com.wetark.main.helper.rocketChat.payload.request.RCLoginUser;
import com.wetark.main.helper.rocketChat.payload.response.RCRegResponse;
import com.wetark.main.helper.rocketChat.payload.request.RCRegisterUser;
import com.wetark.main.helper.rocketChat.RocketChatHelper;
import com.wetark.main.helper.twoFactor.payload.TwoFactorVerifyOtpResponse;
import com.wetark.main.model.user.User;
import com.wetark.main.model.user.UserRepository;
import com.wetark.main.model.user.UserService;
import com.wetark.main.model.user.role.ERole;
import com.wetark.main.model.user.role.Role;
import com.wetark.main.model.user.role.RoleRepository;
import com.wetark.main.payload.request.LoginRequest;
import com.wetark.main.payload.request.SignupRequest;
import com.wetark.main.payload.response.JwtResponse;
import com.wetark.main.payload.response.MessageResponse;
import com.wetark.main.payload.response.user.UserLoginResponse;
import com.wetark.main.security.jwt.JwtUtils;
import com.wetark.main.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	RocketChatHelper rocketChatHelper;

	@Autowired
	UserService userService;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt,
												 userDetails.getId(), 
												 userDetails.getUsername(),
												 userDetails.getEmail(), 
												 roles));
	}

	@GetMapping("/signin/{phoneNo}")
	public ResponseEntity<?> signInByPhoneNo(@PathVariable String phoneNo){
		UserLoginResponse userLoginResponse = userService.signInByPhoneNo(phoneNo);
		return ResponseEntity.ok(userLoginResponse);
	}

	@GetMapping("/verify-otp/{otp}/{sessionId}")
	public ResponseEntity<?> verifyOtp(@PathVariable String otp, @PathVariable String sessionId){
		TwoFactorVerifyOtpResponse twoFactorVerifyOtpResponse = userService.verifyOtp(otp, sessionId);
		return ResponseEntity.ok(twoFactorVerifyOtpResponse);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		Optional<User> userOptional = userRepository.findByPhoneNo(signUpRequest.getPhoneNo());
		if(userOptional.isPresent()){
			User user = userOptional.get();
			if(user.getPassword() != null){
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: PhoneNo is already in use!"));
			}

			user.setName(signUpRequest.getName());
			user.setEmail(signUpRequest.getEmail());
			user.setPassword(encoder.encode(signUpRequest.getPassword()));

			Set<String> strRoles = signUpRequest.getRole();
			Set<Role> roles = new HashSet<>();

			if (strRoles == null) {
				Role userRole = roleRepository.findByName(ERole.ROLE_USER)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(userRole);
			} else {
				strRoles.forEach(role -> {
					switch (role) {
						case "admin":
							Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
									.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
							roles.add(adminRole);

							break;
						case "mod":
							Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
									.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
							roles.add(modRole);

							break;
						default:
							Role userRole = roleRepository.findByName(ERole.ROLE_USER)
									.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
							roles.add(userRole);
					}
				});
			}

			user.setRoles(roles);

			// Create new rc user's account
			RCRegResponse rcRegResponse = rocketChatHelper.CreateUser(
				new RCRegisterUser(
					user.getPhoneNo(),
					user.getEmail(),
					user.getEmail(),
					user.getName()
				)
			);
			if(rcRegResponse.getSuccess() == false){
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse(rcRegResponse.getError()));
			}

			userRepository.save(user);
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(signUpRequest.getPhoneNo(), signUpRequest.getPassword()));
			String jwt = jwtUtils.generateJwtToken(authentication);
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roleList = userDetails.getAuthorities().stream()
					.map(item -> item.getAuthority())
					.collect(Collectors.toList());
			return ResponseEntity.ok(new JwtResponse(jwt,
					user.getId(),
					user.getPhoneNo(),
					user.getEmail(),
					roleList
			));
		}
		return ResponseEntity
				.badRequest()
				.body(new MessageResponse("Error:User with given phoneNo does not exist!"));
	}

	@GetMapping("/me")
	@PreAuthorize("hasRole('USER')")
	public User getUser(){
		return ((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
	}

}
