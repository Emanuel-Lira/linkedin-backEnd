package com.unifacisa.linkedin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.unifacisa.linkedin.user.UserDTO;
import com.unifacisa.linkedin.user.UserServices;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserServices usuarioService;

	@Autowired
	private  PasswordEncoder passwordEncoder;

	@Autowired
	private  AuthenticationManager authenticationManager;

	private JwtUtil jwtUtil;

	private UsuarioDetailsServiceImpl userDetailsService;

	@PostMapping("/register")
//	@PreAuthorize("hasRole('ADMINISTRADOR')")	
	public ResponseEntity<?> register(@RequestBody UserDTO usuario) {
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.saveUser(usuario));
	}

	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		} catch (BadCredentialsException e) {
			System.out.println("BadCredentialsException: " + e.getMessage());
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos", e);
		}

		final UsuarioDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

		final String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new LoginResponse(jwt, userDetails.getId(), userDetails.getUsername()));
	}

}