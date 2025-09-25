package br.com.example.people_graph_service.controller;

import br.com.example.people_graph_service.dto.CompanyGraphDTO;
import br.com.example.people_graph_service.domain.UserNode;
import br.com.example.people_graph_service.dto.UserGraphDTO;
import br.com.example.people_graph_service.service.CompanyGraphService;
import br.com.example.people_graph_service.service.UserGraphService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/graph")
public class GraphController {
    private final UserGraphService userGraphService;
    private final CompanyGraphService companyGraphService;

    public GraphController(UserGraphService userGraphService, CompanyGraphService companyGraphService) {
        this.userGraphService = userGraphService;
        this.companyGraphService = companyGraphService;
    }

    @PostMapping("/users")
    public ResponseEntity<Void> receiveNewUser(@RequestBody UserGraphDTO userDTO) {
        userGraphService.createUserNode(userDTO);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/companies")
    public ResponseEntity<Void> createCompanyNode(@RequestBody CompanyGraphDTO companyDTO) {
        companyGraphService.createCompanyNode(companyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/users/{followerId}/connects/{followedId}")
    public ResponseEntity<Void> userConnectsToUser(@PathVariable Long followerId, @PathVariable Long followedId) {
        userGraphService.createUserConnection(followerId, followedId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/users/{userId}/follows/{companyId}")
    public ResponseEntity<Void> userFollowsCompany(@PathVariable Long userId, @PathVariable Long companyId) {
        userGraphService.createFollowsRelationship(userId, companyId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/users/{userId}/worked-at/{companyId}")
    public ResponseEntity<Void> userWorkedAtCompany(@PathVariable Long userId, @PathVariable Long companyId) {
        userGraphService.createWorkedAtRelationship(userId, companyId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/users/{userId}/suggestions")
    public ResponseEntity<List<UserNode>> getSuggestions(@PathVariable Long userId) {
        return ResponseEntity.ok(userGraphService.getSuggestions(userId));
    }
}