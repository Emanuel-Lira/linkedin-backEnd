package br.com.example.people_graph_service.repository;

import br.com.example.people_graph_service.domain.CompanyNode;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CompanyNodeRepository extends Neo4jRepository<CompanyNode, Long> {
}
