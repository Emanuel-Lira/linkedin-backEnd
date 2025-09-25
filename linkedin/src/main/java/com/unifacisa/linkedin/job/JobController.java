package com.unifacisa.linkedin.job;


import com.unifacisa.linkedin.company.dto.CompanyDTO;
import com.unifacisa.linkedin.job.dto.JobCreateDTO;
import com.unifacisa.linkedin.job.dto.JobResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<JobResponseDTO> createJob(@RequestBody JobCreateDTO dto) {
        Job newJob = jobService.createJob(dto);

        // Mapeia a Company aninhada para o DTO
        CompanyDTO companyDTO = modelMapper.map(newJob.getCompany(), CompanyDTO.class);
        JobResponseDTO response = new JobResponseDTO(newJob, companyDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<JobResponseDTO>> listJobs(Pageable pageable) {
        Page<Job> jobPage = jobService.listAll(pageable);

        Page<JobResponseDTO> responsePage = jobPage.map(job -> {
            CompanyDTO companyDTO = modelMapper.map(job.getCompany(), CompanyDTO.class);
            return new JobResponseDTO(job, companyDTO);
        });

        return ResponseEntity.ok(responsePage);
    }
}
