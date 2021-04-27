package com.epam.esm.repository.specification.impl.order;

import com.epam.esm.model.Purchase;
import com.epam.esm.repository.specification.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class OrderByUserIdSpecification implements Specification<Purchase> {
    private final Long idUser;

    public OrderByUserIdSpecification(Long idUser) {
        this.idUser = idUser;
    }

    @Override
    public Optional<Predicate> toPredicate(Root<Purchase> root, CriteriaQuery<Purchase> query, CriteriaBuilder cb) {
        Predicate predicate = cb.equal(root.get("user").get("id"), idUser);
        return Optional.of(predicate);
    }
}
