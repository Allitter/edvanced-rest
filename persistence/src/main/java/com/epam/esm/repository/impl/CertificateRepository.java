package com.epam.esm.repository.impl;

import com.epam.esm.model.Certificate;
import com.epam.esm.repository.AbstractRepository;
import com.epam.esm.repository.specification.common.ModelByIdSpecification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class CertificateRepository extends AbstractRepository<Certificate> {

    public CertificateRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<Certificate> getEntityType() {
        return Certificate.class;
    }

    @Override
    public Optional<Certificate> remove(long id) {
        Optional<Certificate> certificateOptional = queryFirst(new ModelByIdSpecification<>(id));
        certificateOptional.ifPresent(certificate -> certificate.setRemoved(true));
        return certificateOptional;
    }
}
