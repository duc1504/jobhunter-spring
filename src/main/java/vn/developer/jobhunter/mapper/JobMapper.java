package vn.developer.jobhunter.mapper;

import java.util.List;

import org.mapstruct.*;
import vn.developer.jobhunter.domain.Job;
import vn.developer.jobhunter.domain.Skill;
import vn.developer.jobhunter.domain.request.job.ReqUpdateJobDTO;
import vn.developer.jobhunter.domain.response.job.ResUpdateJobDTO;

@Mapper(componentModel = "spring")
public interface JobMapper {

    // update entity from DTO (ignore null fields)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateJobFromDTO(ReqUpdateJobDTO dto, @MappingTarget Job job);

    // entity -> response
    @Mapping(target = "skills", source = "skills")
    ResUpdateJobDTO toUpdateDTO(Job job);

    // convert List<Skill> -> List<String>
    default List<String> map(List<Skill> skills) {
        if (skills == null) return null;
        return skills.stream()
                .map(Skill::getName)
                .toList();
    }
}
