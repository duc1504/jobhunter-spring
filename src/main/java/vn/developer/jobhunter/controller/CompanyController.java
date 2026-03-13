package vn.developer.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import io.micrometer.core.ipc.http.HttpSender.Response;
import jakarta.validation.Valid;
import vn.developer.jobhunter.domain.Company;
import vn.developer.jobhunter.domain.dto.searchDTO.CompanySearchDTO;
import vn.developer.jobhunter.domain.request.company.ReqUpdateCompanyDTO;
import vn.developer.jobhunter.domain.response.ResultPaginationDTO;
import vn.developer.jobhunter.domain.response.company.ResUpdateCompanyDTO;
import vn.developer.jobhunter.service.CompanyService;
import vn.developer.jobhunter.util.annotation.ApiMessage;
import vn.developer.jobhunter.util.error.IdInvaliException;

import java.util.List;
import java.util.Optional;

import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company reqCompany) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.handleCreateCompany(reqCompany));
    }

    @GetMapping("/companies")
    @ApiMessage(value = "fetch all companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompany(
            CompanySearchDTO companySearchDTO,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(companyService.handleGetAllCompany(companySearchDTO, pageable));
    }

    @PutMapping("/companies")
    public ResponseEntity<ResUpdateCompanyDTO> updateCompany( @Valid @RequestBody ReqUpdateCompanyDTO reqCompany) {
        return ResponseEntity.ok(companyService.handleUpdateCompany(reqCompany));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable long id) {
        if (id <= 0) {
            throw new IdInvaliException("Id must be greater than zero");
        }
        companyService.handleDeleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable long id) {
        Company company = companyService.handleGetCompanyById(id)
                .orElseThrow(() -> new IdInvaliException("Not found by id"));
        return ResponseEntity.ok(company);
    }
}
