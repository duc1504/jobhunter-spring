package vn.developer.jobhunter.domain.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import vn.developer.jobhunter.domain.Company;
import vn.developer.jobhunter.domain.dto.searchDTO.CompanySearchDTO;

public class CompanySpecification {
 public static Specification<Company> buildFilterCompany(CompanySearchDTO filter) {
 return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getName() != null && !filter.getName().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + filter.getName().toLowerCase() + "%"));
            }

            if (filter.getDescription() != null && !filter.getDescription().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("description")),
                                "%" + filter.getDescription().toLowerCase() + "%"));
            }
            if (filter.getAddress() != null && !filter.getAddress().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("address")),
                                "%" + filter.getAddress().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
