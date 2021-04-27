package com.epam.esm.repository.specification.impl.common;

import com.epam.esm.model.Model;
import com.epam.esm.repository.specification.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class AllSpecification<T extends Model> implements Specification<T> {
    @Override
    public Optional<Predicate> toPredicate(Root<T> root, CriteriaQuery<T> query, CriteriaBuilder cb) {
        return Optional.empty();
    }
}
