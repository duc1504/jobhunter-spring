package vn.developer.jobhunter.domain.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;

import vn.developer.jobhunter.domain.Company;
import vn.developer.jobhunter.domain.Job;
import vn.developer.jobhunter.domain.Skill;
import vn.developer.jobhunter.domain.dto.searchDTO.JobSearchDTO;

public class JobSpecification {
public static Specification<Job> buildFilterJob(JobSearchDTO req) {

        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            if (req.getName() != null) {
                predicate = cb.and(predicate,
                        cb.like(root.get("name"), "%" + req.getName() + "%"));
            }

            if (req.getLocation() != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("location"), req.getLocation()));
            }

            if (req.getLevel() != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("level"), req.getLevel()));
            }

            if (req.getActive() != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("active"), req.getActive()));
            }

            if (req.getMinSalary() != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("salary"), req.getMinSalary()));
            }

            if (req.getMaxSalary() != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("salary"), req.getMaxSalary()));
            }

            if (req.getCompanyId() != null) {
                Join<Job, Company> companyJoin = root.join("company", JoinType.INNER);

                predicate = cb.and(predicate,
                        cb.equal(companyJoin.get("id"), req.getCompanyId()));
            }

            if (req.getSkillId() != null) {

                Join<Job, Skill> skillJoin = root.join("skills", JoinType.INNER);

                predicate = cb.and(predicate,
                        cb.equal(skillJoin.get("id"), req.getSkillId()));
            }

            return predicate;
        };
    }
}
