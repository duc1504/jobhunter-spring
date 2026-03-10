package vn.developer.jobhunter.service;

import org.springframework.boot.autoconfigure.rsocket.RSocketProperties.Server.Spec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.developer.jobhunter.domain.Job;
import vn.developer.jobhunter.domain.Resume;
import vn.developer.jobhunter.domain.User;
import vn.developer.jobhunter.domain.dto.searchDTO.ResumeSearchDTO;
import vn.developer.jobhunter.domain.response.ResUserDTO;
import vn.developer.jobhunter.domain.response.ResultPaginationDTO;
import vn.developer.jobhunter.domain.response.resume.ReqCreateResumeDTO;
import vn.developer.jobhunter.domain.response.resume.ResResumeDTO;
import vn.developer.jobhunter.domain.specification.ResumeSpecification;
import vn.developer.jobhunter.mapper.ResumeMapper;
import vn.developer.jobhunter.repository.ResumeRepository;
import vn.developer.jobhunter.util.error.IdInvaliException;

@Service
@RequiredArgsConstructor
public class ResumeService {
    final private ResumeRepository resumeRepository;
    final private UserService userService;
    final private JobService jobService;
    private final ResumeMapper resumeMapper;

    // handle create resume
    public ResResumeDTO handleCreateResume(ReqCreateResumeDTO dto) {
        Resume resume = resumeMapper.toEntity(dto);
        User user = this.userService.handleGetUserById(dto.getUserId());
        resume.setUser(user);
        Job job = this.jobService.handleGetJobById(dto.getJobId());
        resume.setJob(job);
        Resume resumeDB = this.resumeRepository.save(resume);
        return resumeMapper.toDTO(resumeDB);
    }
    // handle update resume
        public ResResumeDTO handleUpdateResume(ReqCreateResumeDTO dto) {
            Resume resume = this.resumeRepository.findById(dto.getId()).orElseThrow( () -> new IdInvaliException("Resume not found"));
            resume.setState(dto.getState());
            Resume resumeDB = this.resumeRepository.save(resume);
            return resumeMapper.toDTO(resumeDB);
    }
    // handle get resume by id
    public ResResumeDTO handleGetResumeById(long id) {
        Resume resume = this.resumeRepository.findById(id).orElseThrow( () -> new IdInvaliException("Resume not found"));
        return resumeMapper.toDTO(resume);
    }
    // delete resume
    public void handleDeleteResume(long id) {
        this.resumeRepository.deleteById(id);
    }
  // get all resume
public ResultPaginationDTO<ResResumeDTO> handleGetAllResume(Pageable pageable, ResumeSearchDTO filter) {
    Specification<Resume> spec = ResumeSpecification.buildFilterResume(filter);
    Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
    Page<ResResumeDTO> resumeDTOPage = pageResume.map(resumeMapper::toDTO);
    ResultPaginationDTO<ResResumeDTO> result = new ResultPaginationDTO<>();
    ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
    meta.setPage(pageResume.getNumber() + 1);
    meta.setPageSize(pageResume.getSize());
    meta.setPages(pageResume.getTotalPages());
    meta.setTotal(pageResume.getTotalElements());
    result.setMeta(meta);
    result.setResult(resumeDTOPage.getContent());
    return result;
}
}
