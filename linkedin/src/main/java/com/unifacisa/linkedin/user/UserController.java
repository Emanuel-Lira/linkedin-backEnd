package com.unifacisa.linkedin.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserServices userServices;

	@Autowired
	private ModelMapper modelMapper;


	
	@GetMapping()
	public List<UserDTO> getAllUsers() {
		List<User> userEntities = userServices.findAllUsers();

		// Converte cada User da lista para um UserDTO
		List<UserDTO> userDTOs = userEntities.stream()
				.map(user -> modelMapper.map(user, UserDTO.class))
				.collect(Collectors.toList());
		return userDTOs;
	}

	@PostMapping()
	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {

		// CHAMANDO O SERVICES QUE JA TA COM A LOGICA DE SALVAR
		UserDTO newUser = userServices.saveUser(userDTO);

		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
}
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
		User userEntity = userServices.findById(id);
        UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);
		return ResponseEntity.ok(userDTO);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(
			@PathVariable Long id,
			@
					AuthenticationPrincipal UserDetails userDetails
	) {
		User currentUser = userServices.findByEmail(userDetails.getUsername());
		userServices.deleteUser(id, currentUser);
		return ResponseEntity.noContent().build();
	}


}
