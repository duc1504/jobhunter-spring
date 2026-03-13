package vn.developer.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.developer.jobhunter.domain.Company;
import vn.developer.jobhunter.domain.User;
import vn.developer.jobhunter.domain.dto.searchDTO.CompanySearchDTO;
import vn.developer.jobhunter.domain.request.company.ReqUpdateCompanyDTO;
import vn.developer.jobhunter.domain.response.ResultPaginationDTO;
import vn.developer.jobhunter.domain.response.company.ResUpdateCompanyDTO;
import vn.developer.jobhunter.domain.specification.CompanySpecification;
import vn.developer.jobhunter.mapper.CompanyMapper;
import vn.developer.jobhunter.repository.CompanyRepository;
import vn.developer.jobhunter.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyMapper companyMapper;

    // create new company
    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    // get all company
    public ResultPaginationDTO<Company> handleGetAllCompany(CompanySearchDTO companySearchDTO, Pageable pageable) {
        Specification<Company> spec = CompanySpecification.buildFilterCompany(companySearchDTO);
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        ResultPaginationDTO<Company> result = new ResultPaginationDTO();
        Page<Company> page = this.companyRepository.findAll(spec, pageable);
        meta.setPage(page.getNumber() + 1);
        meta.setPageSize(page.getSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        result.setMeta(meta);
        result.setResult(page.getContent());
        return result;
    }

    @Transactional
    public ResUpdateCompanyDTO handleUpdateCompany(ReqUpdateCompanyDTO dto) {
        Company company = companyRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        companyMapper.updateCompanyFromDTO(dto, company);
        Company companyUpdate = companyRepository.save(company);
        return companyMapper.toUpdateCompanyDTO(companyUpdate);
    }

    // delete company
    public void handleDeleteCompany(long id) {
        Optional<Company> companyOptional = this.companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            for (User user : companyOptional.get().getUsers()) {
                user.setCompany(null);
                this.userRepository.save(user);
            }
        }
        this.companyRepository.deleteById(id);
    }

    // find by id
    public Optional<Company> handleGetCompanyById(long id) {
        return this.companyRepository.findById(id);
    }
}
