package vn.developer.jobhunter.mapper;

import org.mapstruct.*;

import vn.developer.jobhunter.domain.Company;
import vn.developer.jobhunter.domain.request.company.ReqUpdateCompanyDTO;
import vn.developer.jobhunter.domain.response.company.ResUpdateCompanyDTO;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCompanyFromDTO(ReqUpdateCompanyDTO dto, @MappingTarget Company company);

    ResUpdateCompanyDTO toUpdateCompanyDTO(Company company);

}
