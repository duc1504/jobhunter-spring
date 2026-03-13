package vn.developer.jobhunter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import vn.developer.jobhunter.domain.Resume;
import vn.developer.jobhunter.domain.response.resume.ReqCreateResumeDTO;
import vn.developer.jobhunter.domain.response.resume.ResResumeDTO;

@Mapper(componentModel = "spring")
public interface ResumeMapper {
        @Mapping(source = "user.id", target = "user.id")
        @Mapping(source = "user.name", target = "user.name")
        @Mapping(source = "job.id", target = "job.id")
        @Mapping(source = "job.name", target = "job.name")
        @Mapping(source = "job.company.name", target = "companyName")
        ResResumeDTO toDTO(Resume resume);
    Resume toEntity(ReqCreateResumeDTO dto);


}