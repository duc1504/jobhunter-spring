package vn.developer.jobhunter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.developer.jobhunter.domain.Company;
import vn.developer.jobhunter.domain.dto.searchDTO.CompanySearchDTO;
import vn.developer.jobhunter.domain.response.ResultPaginationDTO;
import vn.developer.jobhunter.domain.specification.CompanySpecification;
import vn.developer.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    
    // create new company
    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }
    // get all company
    public ResultPaginationDTO<List<Company>> handleGetAllCompany (CompanySearchDTO companySearchDTO,Pageable pageable){ 
        Specification<Company> spec = CompanySpecification.buildFilterCompany(companySearchDTO);
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        ResultPaginationDTO<List<Company>> result = new ResultPaginationDTO();
        Page<Company> page = this.companyRepository.findAll(spec,pageable);
        meta.setPage(page.getNumber() + 1);
        meta.setPageSize(page.getSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        result.setMeta(meta);
        result.setResult(page.getContent());
        return result;
    }

    // update company
   public Company handleUpdateCompany(Company reqCompany) {

    Company company = companyRepository.findById(reqCompany.getId())
            .orElseThrow(() -> new RuntimeException("Company not found"));

    company.setName(reqCompany.getName());
    company.setDescription(reqCompany.getDescription());
    company.setAddress(reqCompany.getAddress());
    company.setLogo(reqCompany.getLogo());

    return companyRepository.save(company);
    }

    // delete company
    public void handleDeleteCompany(long id) {
        this.companyRepository.deleteById(id);
    }
}
