package com.epam.esm.repository.impl;

import com.epam.esm.model.Purchase;
import com.epam.esm.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class PurchaseRepository extends AbstractRepository<Purchase> {

    protected PurchaseRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Purchase> getEntityType() {
        return Purchase.class;
    }
}
