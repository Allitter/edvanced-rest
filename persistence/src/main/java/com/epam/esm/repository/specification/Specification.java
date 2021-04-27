package com.epam.esm.repository.specification;

import com.epam.esm.model.Model;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * The Specification interface.
 *
 * @param <T> the type of queried entities
 */
public interface Specification<T extends Model> {

    Optional<Predicate> toPredicate(Root<T> root, CriteriaQuery<T> query, CriteriaBuilder cb);

    static <M extends Model> Specification<M> of(List<Specification<M>> specifications) {
        return (root, query, cb) -> specifications.stream()
                .map(spec -> spec.toPredicate(root, query, cb))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(cb::and);
    }

    static <M extends Model> Specification<M> of(Specification<M>...specifications) {
        return of(List.of(specifications));
    }
}
