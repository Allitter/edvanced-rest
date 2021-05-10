package com.epam.esm.repository.specification.user;

import com.epam.esm.model.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserByLoginSpecification implements Specification<User> {
    private final String login;

    public UserByLoginSpecification(String login) {
        this.login = login;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get("login"), login);
    }
}
