package com.unifacisa.linkedin.job.dto;

import com.unifacisa.linkedin.company.dto.CompanyDTO;
import com.unifacisa.linkedin.job.Job;
import lombok.Data;

@Data
public class JobResponseDTO {
    private Long id;
    private String title;
    private String description;
    private CompanyDTO company;

    public JobResponseDTO(Job job, CompanyDTO companyDTO) {
        this.id = job.getId();
        this.title = job.getTitle();
        this.description = job.getDescription();
        this.company = companyDTO;
    }
}
