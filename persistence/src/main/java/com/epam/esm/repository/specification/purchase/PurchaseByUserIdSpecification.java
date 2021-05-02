package com.epam.esm.repository.specification.purchase;

import com.epam.esm.model.Purchase;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class PurchaseByUserIdSpecification implements Specification<Purchase> {
    private final Long idUser;

    public PurchaseByUserIdSpecification(Long idUser) {
        this.idUser = idUser;
    }

    @Override
    public Predicate toPredicate(Root<Purchase> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get("user").get("id"), idUser);
    }
}
