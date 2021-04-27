package com.epam.esm.repository.impl;

import com.epam.esm.model.Certificate;
import com.epam.esm.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class CertificateRepository extends AbstractRepository<Certificate> {

    public CertificateRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Certificate> getEntityType() {
        return Certificate.class;
    }
}
