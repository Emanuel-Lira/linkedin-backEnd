package br.com.example.people_graph_service.service;

import br.com.example.people_graph_service.domain.CompanyNode;
import br.com.example.people_graph_service.dto.CompanyGraphDTO;
import br.com.example.people_graph_service.repository.CompanyNodeRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyGraphService {
    private final CompanyNodeRepository repository;

    public CompanyGraphService(CompanyNodeRepository repository) {
        this.repository = repository;
    }

    public void createCompanyNode(CompanyGraphDTO dto) {
        CompanyNode node = new CompanyNode(dto.getId(), dto.getName());
        repository.save(node);
    }
}