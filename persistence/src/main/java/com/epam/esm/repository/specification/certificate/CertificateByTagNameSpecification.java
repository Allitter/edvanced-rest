package com.epam.esm.repository.specification.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class CertificateByTagNameSpecification implements Specification<Certificate> {
    private final String tagName;

    public CertificateByTagNameSpecification(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public Predicate toPredicate(Root<Certificate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Join<Tag, Certificate> certificateTagJoin = root.join("tags");
        ((CriteriaQuery<Certificate>)query).select(root).distinct(true);
        return criteriaBuilder.like(certificateTagJoin.get("name"), "%" + tagName + "%");
    }
}
