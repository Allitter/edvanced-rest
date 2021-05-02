package com.epam.esm.repository.impl;

import com.epam.esm.model.Certificate;
import com.epam.esm.repository.AbstractRepository;
import com.epam.esm.repository.specification.common.ModelByIdSpecification;
import com.epam.esm.repository.specification.common.ModelNotRemovedSpecification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
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
    protected void fetchConnectedEntities(Root<Certificate> root) {
        root.fetch("tags", JoinType.LEFT);
    }


    @Override
    public Optional<Certificate> remove(long id) {
        Optional<Certificate> certificateOptional = queryFirst(
                new ModelByIdSpecification<Certificate>(id).and(new ModelNotRemovedSpecification<>()));

        certificateOptional.ifPresent(certificate -> certificate.setRemoved(true));
        return certificateOptional;
    }
}
