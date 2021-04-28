package com.epam.esm.repository.impl;

import com.epam.esm.model.Purchase;
import com.epam.esm.repository.AbstractRepository;
import com.epam.esm.repository.specification.common.ModelByIdSpecification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class PurchaseRepository extends AbstractRepository<Purchase> {

    protected PurchaseRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Purchase> getEntityType() {
        return Purchase.class;
    }

    @Override
    public Optional<Purchase> remove(long id) {
        Optional<Purchase> purchaseOptional = queryFirst(new ModelByIdSpecification<>(id));
        purchaseOptional.ifPresent(purchase -> purchase.setRemoved(true));
        return purchaseOptional;
    }
}
