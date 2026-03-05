package vn.developer.jobhunter.domain.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import vn.developer.jobhunter.domain.Skill;
import vn.developer.jobhunter.domain.dto.searchDTO.SkillSearchDTO;
import vn.developer.jobhunter.domain.dto.searchDTO.UserSearchDTO;

public class SkillSpecifation {
     public static Specification<Skill> buildFilterSkill(SkillSearchDTO filter) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getName() != null && !filter.getName().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + filter.getName().toLowerCase() + "%"));
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
