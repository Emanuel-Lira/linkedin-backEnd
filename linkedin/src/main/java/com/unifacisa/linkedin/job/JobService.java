package com.unifacisa.linkedin.job;



import com.unifacisa.linkedin.company.Company;
import com.unifacisa.linkedin.company.CompanyRepository;
import com.unifacisa.linkedin.job.dto.JobCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public Job createJob(JobCreateDTO dto) {
        // Busca a empresa pelo ID fornecido. Se não encontrar, lança erro.
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));

        Job newJob = new Job();
        newJob.setTitle(dto.getTitle());
        newJob.setDescription(dto.getDescription());
        newJob.setCompany(company); // Associa a vaga à empresa

        return jobRepository.save(newJob);
    }

    public Page<Job> listAll(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }
}
