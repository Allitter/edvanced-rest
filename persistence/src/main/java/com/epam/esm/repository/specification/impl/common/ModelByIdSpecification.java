package com.epam.esm.repository.specification.impl.common;

import com.epam.esm.model.Model;
import com.epam.esm.repository.specification.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class ModelByIdSpecification<T extends Model> implements Specification<T> {
    private final long id;

    public ModelByIdSpecification(long id) {
        this.id = id;
    }

    @Override
    public Optional<Predicate> toPredicate(Root<T> root, CriteriaQuery<T> query, CriteriaBuilder cb) {
        Predicate predicate = cb.equal(root.get("id"), id);
        return Optional.of(predicate);
    }
}
