package com.epam.esm.repository;

import com.epam.esm.exception.SortArgumentException;
import com.epam.esm.model.Model;
import com.epam.esm.repository.query.NativeQuery;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractRepository<T extends Model> implements MainRepository<T> {

    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    protected final EntityManager entityManager;

    protected AbstractRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected abstract Class<T> getEntityType();

    @Override
    public T add(T model) {
        model.setId(null);
        entityManager.persist(model);
        return model;
    }

    @Override
    public Collection<T> addAll(Collection<T> models) {
        models.forEach(model -> model.setId(null));
        models.forEach(entityManager::persist);
        entityManager.flush();
        return new ArrayList<>(models);
    }

    @Override
    public T update(T model) {
        return entityManager.merge(model);
    }

    @Override
    public Optional<T> remove(long id) {
        T model = entityManager.find(getEntityType(), id);
        if (model != null) {
            entityManager.remove(model);
        }
        return Optional.ofNullable(model);
    }

    @Override
    public Optional<T> queryFirst(Specification<T> specification) {
        List<T> list = queryList(specification, Pageable.unpaged());
        return Optional.ofNullable(DataAccessUtils.singleResult(list));
    }

    @Override
    public Optional<T> queryFirst(NativeQuery nativeQuery) {
        List<T> list = queryList(nativeQuery, Pageable.unpaged());
        return Optional.ofNullable(DataAccessUtils.singleResult(list));
    }

    @Override
    public Page<T> query(Specification<T> specification, Pageable pageable) {
        List<T> items = queryList(specification, pageable);
        return new PageImpl<>(items, pageable, count(specification));
    }

    @Override
    public Page<T> query(NativeQuery nativeQuery, Pageable pageable) {
        List<T> items = queryList(nativeQuery, pageable);
        return new PageImpl<>(items, pageable, count(nativeQuery));
    }

    public List<T> queryList(NativeQuery nativeQuery, Pageable pageable) {
        Query query = entityManager.createNativeQuery(nativeQuery.getQuery(), getEntityType());

        Map<String, Object> params = nativeQuery.getParams();
        params.keySet().forEach(key -> query.setParameter(key, params.get(key)));

        setResultPageIfPaged(pageable, query);

        return query.getResultList();
    }

    public List<T> queryList(Specification<T> specification, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getEntityType());
        Root<T> root = criteriaQuery.from(getEntityType());

        criteriaQuery.where(specification.toPredicate(root, criteriaQuery, criteriaBuilder));

        if (pageable.isPaged() && pageable.getSort().isSorted()) {
            List<Order> orders = getOrdersFromPageable(pageable, criteriaBuilder, root);
            criteriaQuery.orderBy(orders);
        }

        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
        setResultPageIfPaged(pageable, query);

        return query.getResultList();
    }

    private void setResultPageIfPaged(Pageable pageable, Query query) {
        if (pageable.isPaged()) {
            int page = pageable.getPageNumber();
            int pageSize = pageable.getPageSize();
            query.setFirstResult(page * pageSize)
                    .setMaxResults(pageSize);
        }
    }

    private List<Order> getOrdersFromPageable(Pageable pageable, CriteriaBuilder criteriaBuilder, Root<T> root) {
        return pageable.getSort().stream()
                .map(order -> getSortOrder(order, criteriaBuilder, root))
                .collect(Collectors.toList());
    }

    private Order getSortOrder(Sort.Order order, CriteriaBuilder criteriaBuilder, Root<T> root) {
        try {
            return order.getDirection() == Sort.Direction.ASC
                    ? criteriaBuilder.asc(root.get(order.getProperty()))
                    : criteriaBuilder.desc(root.get(order.getProperty()));
        } catch (IllegalArgumentException e) {
            throw new SortArgumentException(order.getProperty());
        }
    }

    private long count(Specification<T> specification) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getEntityType());
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<T> root = countQuery.from(getEntityType());

        countQuery.select(criteriaBuilder.count(root));
        countQuery.where(specification.toPredicate(root, criteriaQuery, criteriaBuilder));

        return entityManager
                .createQuery(countQuery)
                .getSingleResult();
    }

    private long count(NativeQuery nativeQuery) {
        Query query = entityManager.createNativeQuery(nativeQuery.getQuery(), Long.class);
        Map<String, Object> params = nativeQuery.getParams();

        params.keySet().forEach(key -> query.setParameter(key, params.get(key)));

        return (long) query.getSingleResult();
    }
}
