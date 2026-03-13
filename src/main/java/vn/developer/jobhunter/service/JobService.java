package vn.developer.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.developer.jobhunter.domain.Company;
import vn.developer.jobhunter.domain.Job;
import vn.developer.jobhunter.domain.Skill;
import vn.developer.jobhunter.domain.dto.searchDTO.JobSearchDTO;
import vn.developer.jobhunter.domain.request.job.ReqUpdateJobDTO;
import vn.developer.jobhunter.domain.response.ResultPaginationDTO;
import vn.developer.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.developer.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.developer.jobhunter.domain.specification.JobSpecification;
import vn.developer.jobhunter.mapper.JobMapper;
import vn.developer.jobhunter.repository.CompanyRepository;
import vn.developer.jobhunter.repository.JobRepository;
import vn.developer.jobhunter.repository.SkillRepository;
import vn.developer.jobhunter.util.error.IdInvaliException;
import vn.developer.jobhunter.util.error.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final SkillRepository skillRepository;
    private final JobMapper jobMapper;

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
        if (jobDB.getSkills() != null) {
            resCreateJobDTO.setSkills(jobDB.getSkills().stream().map(Skill::getName).collect(Collectors.toList()));
        }
        return resCreateJobDTO;
    }

    // update job
    @Transactional
    public ResUpdateJobDTO handleUpdateJob(ReqUpdateJobDTO dto) {
        Job jobDB = jobRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Job not found"));
        // update basic fields
        jobMapper.updateJobFromDTO(dto, jobDB);
        // update company
        if (dto.getCompanyId() != null) {
            Company company = companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            jobDB.setCompany(company);
        }
        // update skills
        if (dto.getSkillIds() != null) {
            List<Skill> skills = skillRepository.findAllById(dto.getSkillIds());
            jobDB.setSkills(skills);
        }
        Job jobUpdate = jobRepository.save(jobDB);
        return jobMapper.toUpdateDTO(jobUpdate);
    }
    // delete job
    @Transactional
    public void handleDeleteJob(long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.getSkills().clear();
        jobRepository.delete(job);
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

    // get job by id
    public Job handleGetJobById(long id) {
        return this.jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id= " + id));
    }
}
