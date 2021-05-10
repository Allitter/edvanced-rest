package com.epam.esm.repository.impl;

import com.epam.esm.model.Purchase;
import com.epam.esm.repository.AbstractRepository;
import com.epam.esm.repository.specification.common.ModelByIdSpecification;
import com.epam.esm.repository.specification.common.ModelNotRemovedSpecification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
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
    protected void fetchConnectedEntities(Root<Purchase> root) {
        root.fetch("purchaseCertificates", JoinType.LEFT);
    }

    @Override
    public Optional<Purchase> remove(long id) {
        Optional<Purchase> purchaseOptional = queryFirst(
                new ModelByIdSpecification<Purchase>(id).and(new ModelNotRemovedSpecification<>()));

        purchaseOptional.ifPresent(purchase -> purchase.setRemoved(true));
        return purchaseOptional;
    }
}
