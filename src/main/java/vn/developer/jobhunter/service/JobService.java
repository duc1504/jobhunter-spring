package vn.developer.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import vn.developer.jobhunter.domain.Company;
import vn.developer.jobhunter.domain.Job;
import vn.developer.jobhunter.domain.Skill;
import vn.developer.jobhunter.domain.dto.searchDTO.JobSearchDTO;
import vn.developer.jobhunter.domain.response.ResultPaginationDTO;
import vn.developer.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.developer.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.developer.jobhunter.domain.specification.JobSpecification;
import vn.developer.jobhunter.repository.CompanyRepository;
import vn.developer.jobhunter.repository.JobRepository;
import vn.developer.jobhunter.repository.SkillRepository;
import vn.developer.jobhunter.util.error.IdInvaliException;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, CompanyRepository companyRepository,
            SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.skillRepository = skillRepository;
    }

    // create new job
    @Transactional
    public ResCreateJobDTO handleCreateJob(Job job) {
        // check company
        if (job.getCompany() != null) {
            Company company = this.companyRepository.findById(job.getCompany().getId())
                    .orElseThrow(() -> new IdInvaliException("Company not found"));
            job.setCompany(company);
        }
        // check skill
        if (job.getSkills() != null && !job.getSkills().isEmpty()) {
            List<Long> skillIds = job.getSkills().stream().map(Skill::getId).collect(Collectors.toList());
            List<Skill> skillsDB = this.skillRepository.findAllById(skillIds);
            job.setSkills(skillsDB);
        }
        Job jobDB = this.jobRepository.save(job);
        // mapping dto
        ResCreateJobDTO resCreateJobDTO = new ResCreateJobDTO();
        resCreateJobDTO.setId(jobDB.getId());
        resCreateJobDTO.setName(jobDB.getName());
        resCreateJobDTO.setLocation(jobDB.getLocation());
        resCreateJobDTO.setSalary(jobDB.getSalary());
        resCreateJobDTO.setQuantity(jobDB.getQuantity());
        resCreateJobDTO.setLevel(jobDB.getLevel());
        resCreateJobDTO.setStartDate(jobDB.getStartDate());
        resCreateJobDTO.setEndDate(jobDB.getEndDate());
        resCreateJobDTO.setActive(jobDB.isActive());
        resCreateJobDTO.setCreatedAt(jobDB.getCreatedAt());
        resCreateJobDTO.setCreatedBy(jobDB.getCreatedBy());
        return resCreateJobDTO;
    }

    // update job
    @Transactional
    public ResUpdateJobDTO handleUpdateJob(Job job) {

        Job jobDB = this.jobRepository.findById(job.getId()).orElseThrow(() -> new RuntimeException("Job not found"));
        // check company
        if (job.getCompany() != null) {
            Company company = this.companyRepository.findById(job.getCompany().getId())
                    .orElseThrow(() -> new IdInvaliException("Company not found"));
            jobDB.setCompany(company);
        }
        // check skill
        if (job.getSkills() != null) {
            List<Long> skillIds = job.getSkills().stream().map(Skill::getId).collect(Collectors.toList());
            List<Skill> skillsDB = this.skillRepository.findAllById(skillIds);
            jobDB.setSkills(skillsDB);
        }
        // update
        jobDB.setName(job.getName());
        jobDB.setSalary(job.getSalary());
        jobDB.setQuantity(job.getQuantity());
        jobDB.setLocation(job.getLocation());
        jobDB.setLevel(job.getLevel());
        jobDB.setStartDate(job.getStartDate());
        jobDB.setEndDate(job.getEndDate());
        jobDB.setActive(job.isActive());
        Job jobUpdate = this.jobRepository.save(jobDB);

        // mapping resupdatejobdto
        ResUpdateJobDTO dto = new ResUpdateJobDTO();
        dto.setId(jobUpdate.getId());
        dto.setName(jobUpdate.getName());
        dto.setSalary(jobUpdate.getSalary());
        dto.setQuantity(jobUpdate.getQuantity());
        dto.setLocation(jobUpdate.getLocation());
        dto.setLevel(jobUpdate.getLevel());
        dto.setStartDate(jobUpdate.getStartDate());
        dto.setEndDate(jobUpdate.getEndDate());
        dto.setActive(jobUpdate.isActive());
        dto.setUpdatedAt(jobUpdate.getUpdatedAt());
        dto.setUpdatedBy(jobUpdate.getUpdatedBy());
        if (jobUpdate.getSkills() != null) {
            dto.setSkills(jobUpdate.getSkills().stream().map(Skill::getName).collect(Collectors.toList()));
        }
        return dto;
    }

    // delete job
    public void handleDeleteJob(long id) {
        this.jobRepository.findById(id).orElseThrow(() -> new IdInvaliException("Job not found"));
    }
    
    // get all job 
    public ResultPaginationDTO handleGetAllJob(JobSearchDTO filter, Pageable pageable) {
        Specification<Job> spec = JobSpecification.buildFilterJob(filter);
        Page<Job> pageUser = this.jobRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pageUser.getContent());

        return rs;
    }
}
