package com.epam.esm.repository.specification.common;

import com.epam.esm.model.Model;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ModelByIdSpecification<T extends Model> implements Specification<T> {
    private final long id;

    public ModelByIdSpecification(long id) {
        this.id = id;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get("id"), id);
    }
}
