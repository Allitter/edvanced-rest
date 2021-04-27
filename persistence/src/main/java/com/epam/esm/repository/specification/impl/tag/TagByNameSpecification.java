package com.epam.esm.repository.specification.impl.tag;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.specification.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class TagByNameSpecification implements Specification<Tag> {
    private final String name;

    public TagByNameSpecification(String name) {
        this.name = name;
    }

    @Override
    public Optional<Predicate> toPredicate(Root<Tag> root, CriteriaQuery<Tag> query, CriteriaBuilder cb) {
        Predicate predicate = cb.like(root.get("name"), name);
        return Optional.of(predicate);
    }
}
