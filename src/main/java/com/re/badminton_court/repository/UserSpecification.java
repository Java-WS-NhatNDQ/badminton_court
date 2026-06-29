package com.re.badminton_court.repository;

import com.re.badminton_court.model.dto.user.UserSearchRequest;
import com.re.badminton_court.model.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> withCriteria(UserSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
                String pattern = "%" + request.getKeyword().trim() + "%";
                Predicate usernameLike = cb.like(root.get("username"), pattern);
                Predicate fullNameLike = cb.like(root.get("fullName"), pattern);
                Predicate emailLike = cb.like(root.get("email"), pattern);
                Predicate phoneLike = cb.like(root.get("phoneNumber"), pattern);
                predicates.add(cb.or(usernameLike, fullNameLike, emailLike, phoneLike));
            }

            if (request.getRole() != null) {
                predicates.add(cb.equal(root.get("role"), request.getRole()));
            }

            if (request.getEnabled() != null) {
                predicates.add(cb.equal(root.get("enabled"), request.getEnabled()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
