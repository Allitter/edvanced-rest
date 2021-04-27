package com.epam.esm.repository.specification.impl.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.specification.Specification;

import javax.persistence.criteria.*;
import java.util.Optional;

public class CertificateByTagNameSpecification implements Specification<Certificate> {
    private final String tagName;

    public CertificateByTagNameSpecification(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public Optional<Predicate> toPredicate(Root<Certificate> root,
                                           CriteriaQuery<Certificate> criteriaQuery,
                                           CriteriaBuilder criteriaBuilder) {
        Join<Tag, Certificate> certificateTagJoin = root.join("tags");
        criteriaQuery.select(root).distinct(true);
        Predicate predicate = criteriaBuilder
                .like(certificateTagJoin.get("name"), "%" + tagName + "%");
        return Optional.of(predicate);
    }
}
