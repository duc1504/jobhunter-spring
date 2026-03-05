package vn.developer.jobhunter.controller;

import org.springframework.data.domain.Pageable;
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
import vn.developer.jobhunter.domain.Skill;
import vn.developer.jobhunter.domain.dto.searchDTO.SkillSearchDTO;
import vn.developer.jobhunter.domain.response.ResSkillDTO;
import vn.developer.jobhunter.domain.response.ResultPaginationDTO;
import vn.developer.jobhunter.service.SkillService;
import vn.developer.jobhunter.util.annotation.ApiMessage;
import vn.developer.jobhunter.util.error.IdInvaliException;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }
    // create new skill
     @PostMapping("/skills")
     @ApiMessage("Create a skill")
     public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skill) {
        return ResponseEntity.status(201).body(skillService.handleCreateSkill(skill));
     }

     // update skill
    @PutMapping("/skills")
    @ApiMessage("Update a skill")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill) {
        return ResponseEntity.status(200).body(skillService.handleUpdateSkill(skill));
    }


    // delete skill
    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> deleteSkill(@PathVariable long id) throws IdInvaliException {
    skillService.handleDeleteSkill(id);
    return ResponseEntity.status(200).build();
    }


    // get all skill
    @GetMapping("/skills")
    @ApiMessage("fetch all skills")
    public ResponseEntity<ResultPaginationDTO> getAllSkill(SkillSearchDTO filter ,Pageable pageable) {
        return ResponseEntity.status(200).body(skillService.handleGetAllSkill(filter,pageable));
    }


}
