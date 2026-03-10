package vn.developer.jobhunter.domain.specification;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.JoinType;
import vn.developer.jobhunter.domain.Resume;
import vn.developer.jobhunter.domain.Skill;
import vn.developer.jobhunter.domain.dto.searchDTO.ResumeSearchDTO;

public class ResumeSpecification {
 public static Specification<Resume> buildFilterResume(ResumeSearchDTO req) {

        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            if (req.getEmail() != null) {
                predicate = cb.and(predicate,
                        cb.like(root.get("email"), "%" + req.getEmail() + "%"));
            }

            if (req.getState() != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("state"), req.getState()));
            }
            return predicate;
        };
    }
}
