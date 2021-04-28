package com.epam.esm.repository.specification.tag;

import com.epam.esm.model.Tag;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TagByNameSpecification implements Specification<Tag> {
    private final String name;

    public TagByNameSpecification(String name) {
        this.name = name;
    }

    @Override
    public Predicate toPredicate(Root<Tag> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.like(root.get("name"), name);
    }
}
