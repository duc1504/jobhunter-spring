package vn.developer.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.developer.jobhunter.domain.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill , Long> , JpaSpecificationExecutor<Skill> {
    Boolean existsByName(String name);
}