package com.epam.esm.repository.specification.tag;

import com.epam.esm.model.Tag;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TagNameInSpecification implements Specification<Tag> {
    private final List<String> names;

    public TagNameInSpecification(Collection<String> names) {
        this.names = new ArrayList<>(names);
    }

    @Override
    public Predicate toPredicate(Root<Tag> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return root.get("name").in(names);
    }
}
