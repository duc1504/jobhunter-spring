package vn.developer.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.developer.jobhunter.domain.Job;
import vn.developer.jobhunter.domain.dto.searchDTO.JobSearchDTO;
import vn.developer.jobhunter.domain.dto.searchDTO.UserSearchDTO;
import vn.developer.jobhunter.domain.response.ResultPaginationDTO;
import vn.developer.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.developer.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.developer.jobhunter.service.JobService;
import vn.developer.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("Create a job")
    public ResponseEntity<ResCreateJobDTO> createJob(@Valid @RequestBody Job job) {
        return ResponseEntity.status(201).body(jobService.handleCreateJob(job));
    }

    // update job
    @PutMapping("/jobs")
    @ApiMessage("Create a job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@Valid @RequestBody Job job) {
        return ResponseEntity.status(201).body(jobService.handleUpdateJob(job));
    }

    // delete job
    @DeleteMapping("/jobs/{id}")
    @ApiMessage("delete job")
    public ResponseEntity<Void> deleteJob(@PathVariable long id) {
        jobService.handleDeleteJob(id);
        return ResponseEntity.ok(null);
    }

    // get all job
    @GetMapping("/jobs")
    @ApiMessage("fetch all jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJob(
        JobSearchDTO filter,
        Pageable pageable
    ) {
        
         return ResponseEntity.status(HttpStatus.OK).body(jobService.handleGetAllJob(filter,pageable));
    }

    
}
