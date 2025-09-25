package br.com.example.people_graph_service.service;

import br.com.example.people_graph_service.domain.UserNode;
import br.com.example.people_graph_service.dto.UserGraphDTO;
import br.com.example.people_graph_service.repository.UserNodeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserGraphService {
    private final UserNodeRepository userRepository;

    public UserGraphService(UserNodeRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUserNode(UserGraphDTO dto) {
        UserNode node = new UserNode(dto.getId(), dto.getName());
        userRepository.save(node);
    }
    public void createUserConnection(Long followerId, Long followedId) {
        userRepository.userConnectsToUser(followerId, followedId);
    }
    public void createFollowsRelationship(Long userId, Long companyId) {
        userRepository.userFollowsCompany(userId, companyId);
    }
    public void createWorkedAtRelationship(Long userId, Long companyId) {
        userRepository.userWorkedAtCompany(userId, companyId);
    }
    public List<UserNode> getSuggestions(Long userId) {
        return userRepository.findSecondDegreeConnections(userId);
    }
}