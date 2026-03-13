package vn.developer.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.developer.jobhunter.domain.dto.searchDTO.ResumeSearchDTO;
import vn.developer.jobhunter.domain.response.ResultPaginationDTO;
import vn.developer.jobhunter.domain.response.resume.ReqCreateResumeDTO;
import vn.developer.jobhunter.domain.response.resume.ResResumeDTO;
import vn.developer.jobhunter.mapper.ResumeMapper;
import vn.developer.jobhunter.service.ResumeService;
import vn.developer.jobhunter.util.annotation.ApiMessage;
import vn.developer.jobhunter.util.error.IdInvaliException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ResumeController {
    final private ResumeService resumeService;
    //  create new resume
    @PostMapping("/resumes")
    @ApiMessage("Create a resume")
    public ResponseEntity<ResResumeDTO> createResume(@Valid @RequestBody ReqCreateResumeDTO dto) {
        ResResumeDTO resResumeDTO = this.resumeService.handleCreateResume(dto);
        return ResponseEntity.ok(resResumeDTO);
    }

    // update resume
        @PutMapping("/resumes")
        @ApiMessage("Update a resume")
        public ResponseEntity<ResResumeDTO> updateResume(@RequestBody ReqCreateResumeDTO dto) {
            ResResumeDTO resResumeDTO = this.resumeService.handleUpdateResume(dto);
            return ResponseEntity.ok(resResumeDTO);
        }
    
        // get resume by id
        @GetMapping("/resumes/{id}")
        @ApiMessage("Get resume by id")
        public ResponseEntity<ResResumeDTO> getResumeById(@PathVariable long id) {
            ResResumeDTO resResumeDTO = this.resumeService.handleGetResumeById(id);
            return ResponseEntity.ok(resResumeDTO);
        }

        // delete resume
        @DeleteMapping("/resumes/{id}")
        @ApiMessage("Delete a resume")
        public ResponseEntity<Void> deleteResume(@PathVariable long id)  throws IdInvaliException {
            this.resumeService.handleDeleteResume(id);
            return ResponseEntity.ok(null);
        }
        // get all resume
        @GetMapping("/resumes")
        @ApiMessage("Get all resume")
    public ResultPaginationDTO<ResResumeDTO> getAllResume(
            Pageable pageable,ResumeSearchDTO filter) {
        return resumeService.handleGetAllResume(pageable, filter);
    }


}
