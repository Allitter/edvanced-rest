package com.epam.esm.repository.specification.certificate;

import com.epam.esm.model.Certificate;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CertificateByDescriptionSpecification implements Specification<Certificate> {
    private final String description;

    public CertificateByDescriptionSpecification(String description) {
        this.description = description;
    }

    @Override
    public Predicate toPredicate(Root<Certificate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.like(root.get("description"), "%" + description + "%");
    }
}
