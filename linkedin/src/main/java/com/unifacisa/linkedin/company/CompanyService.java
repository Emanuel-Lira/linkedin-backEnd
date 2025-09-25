package com.unifacisa.linkedin.company;

import com.unifacisa.linkedin.company.dto.CompanyDTO;
import com.unifacisa.linkedin.company.dto.CompanyGraphDTO; // Importe o novo DTO
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate; // Importe o RestTemplate

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RestTemplate restTemplate; // Injete o "telefone"

    public CompanyDTO createCompany(CompanyDTO dto) {
        Company company = modelMapper.map(dto, Company.class);
        company = companyRepository.save(company);

        // =======================================================
        // ## A PONTE PARA O GRAPH-SERVICE ##
        try {
            String graphServiceUrl = "http://localhost:8081/graph/companies";
            CompanyGraphDTO graphPayload = new CompanyGraphDTO(company.getId(), company.getName());

            restTemplate.postForEntity(graphServiceUrl, graphPayload, Void.class);
            System.out.println(">>> [Core Service] Notificação de EMPRESA enviada para o Graph Service!");

        } catch (Exception e) {
            System.err.println("AVISO: Falha ao comunicar com o Graph Service para criar nó de Empresa. Causa: " + e.getMessage());
        }
        // =======================================================

        return modelMapper.map(company, CompanyDTO.class);
    }

    public Page<CompanyDTO> listAll(Pageable pageable) {
        Page<Company> companiesPage = companyRepository.findAll(pageable);
        return companiesPage.map(company -> modelMapper.map(company, CompanyDTO.class));
    }
}