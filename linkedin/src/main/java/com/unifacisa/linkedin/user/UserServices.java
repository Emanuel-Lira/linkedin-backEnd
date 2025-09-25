package com.unifacisa.linkedin.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserServices {

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RestTemplate restTemplate;

	public UserDTO saveUser(UserDTO user) {
		User userEntity = modelMapper.map(user, User.class);

		userEntity = userRepository.save(userEntity);

		// 4. ================ CONEXÃO COM O NEO4J ================
		try {
			String graphServiceUrl = "http://localhost:8081/graph/users";
			UserGraphDTO graphPayload = new UserGraphDTO(userEntity.getId(), userEntity.getName());
			restTemplate.postForEntity(graphServiceUrl, graphPayload, Void.class);
			System.out.println(">>>  Notificação enviada para o Graph Service!");
		} catch (Exception e) {
			System.err.println("AVISO: Falha ao comunicar com o Graph Service. Causa: " + e.getMessage());
		}
		// ===========================================

		return modelMapper.map(userEntity, UserDTO.class);
	}

	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	public void deleteUser(Long idToDelete, User currentUser) {
		// 1. Verifica se o utilizador que está a tentar apagar
		//    é o mesmo que está logado.
		if (!idToDelete.equals(currentUser.getId())) {
			// Se não for, lança um erro 403 Forbidden (Proibido).
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Utilizador não tem permissão para apagar esta conta");
		}

		// Verificando se realmente existe antes de tentar apagar.
		User userToDelete = userRepository.findById(idToDelete)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilizador não encontrado"));


		userRepository.delete(userToDelete);
	}


	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Usuário com e-mail " + email + " não encontrado"));
	}

	public User findById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
	}

	public Optional<User> findByName(String username) {
		return userRepository.findByName(username);
	}
}