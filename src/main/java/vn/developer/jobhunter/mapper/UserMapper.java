package vn.developer.jobhunter.mapper;

import org.mapstruct.*;
import vn.developer.jobhunter.domain.User;
import vn.developer.jobhunter.domain.request.user.ReqUpdateUserDTO;
import vn.developer.jobhunter.domain.response.RestUpdateUserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDTO(ReqUpdateUserDTO dto, @MappingTarget User user);

    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "company.id", target = "company.id")
    @Mapping(source = "company.name", target = "company.name")
    RestUpdateUserDTO toUpdateUserDTO(User user);


}
