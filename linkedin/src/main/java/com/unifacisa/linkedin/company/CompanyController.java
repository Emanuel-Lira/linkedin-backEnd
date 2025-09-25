package com.unifacisa.linkedin.company;


import com.unifacisa.linkedin.company.dto.CompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyDTO dto) {
        CompanyDTO newCompany = companyService.createCompany(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }

    @GetMapping
    public ResponseEntity<Page<CompanyDTO>> listCompanies(Pageable pageable) {
        return ResponseEntity.ok(companyService.listAll(pageable));
    }
}
