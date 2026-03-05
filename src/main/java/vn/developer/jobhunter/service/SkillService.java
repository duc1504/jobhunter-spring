package vn.developer.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Meter.Id;
import vn.developer.jobhunter.domain.Skill;
import vn.developer.jobhunter.domain.dto.searchDTO.SkillSearchDTO;
import vn.developer.jobhunter.domain.response.ResSkillDTO;
import vn.developer.jobhunter.domain.response.ResultPaginationDTO;
import vn.developer.jobhunter.domain.specification.SkillSpecifation;
import vn.developer.jobhunter.repository.SkillRepository;
import vn.developer.jobhunter.util.error.IdInvaliException;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }
    // create new skill
    public Skill handleCreateSkill(Skill skill) {
        Boolean nameExists = skillRepository.existsByName(skill.getName());
        if (nameExists) {
            throw new IdInvaliException("đã tồn tại skill" + skill.getName());
        }
        return skillRepository.save(skill);
    }

    // update skill
    public Skill handleUpdateSkill(Skill skill) {
        Optional<Skill> skillOptional = skillRepository.findById(skill.getId());
        if (skillOptional.isEmpty()) {
            throw new IdInvaliException("skill not found");
        }
         Boolean nameExists = skillRepository.existsByName(skill.getName());
        if (nameExists) {
            throw new IdInvaliException("đã tồn tại skill" + skill.getName());
        }
        skillOptional.get().setName(skill.getName());
        return skillRepository.save(skillOptional.get());
    }

    // delete skill
    public void handleDeleteSkill(long id) {
         Optional<Skill> skillOptional = skillRepository.findById(id);
        if (skillOptional.isEmpty()) {
            throw new IdInvaliException("skill not found");
        }
        Skill skill = skillOptional.get();
    if (skill.getJobs() != null) {
        skill.getJobs().forEach(job -> {
            job.getSkills().remove(skill);
        });
    }
    skillRepository.delete(skill);
    }

    //get all skill
    public ResultPaginationDTO<ResSkillDTO> handleGetAllSkill (SkillSearchDTO skillSearchDTO,Pageable pageable){
        Specification<Skill> specification = SkillSpecifation.buildFilterSkill(skillSearchDTO);
        Page<Skill> page = skillRepository.findAll(specification,pageable);
        Page<ResSkillDTO> skillDTO = page.map(skill -> new ResSkillDTO(skill.getId(),skill.getName()));
        ResultPaginationDTO<ResSkillDTO> result = new ResultPaginationDTO<>();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(page.getNumber() + 1);
        meta.setPageSize(page.getSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        result.setMeta(meta);
        result.setResult(skillDTO.getContent());
        return result;
    }


}
