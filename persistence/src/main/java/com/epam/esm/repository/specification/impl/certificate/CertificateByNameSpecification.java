package com.epam.esm.repository.specification.impl.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.repository.specification.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class CertificateByNameSpecification implements Specification<Certificate> {
    private final String name;

    public CertificateByNameSpecification(String name) {
        this.name = name;
    }

    @Override
    public Optional<Predicate> toPredicate(Root<Certificate> root, CriteriaQuery<Certificate> query, CriteriaBuilder cb) {
        Predicate predicate = cb.like(root.get("name"), "%" + name + "%");
        return Optional.of(predicate);
    }

}
