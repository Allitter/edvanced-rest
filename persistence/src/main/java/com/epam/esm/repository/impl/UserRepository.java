package com.epam.esm.repository.impl;

import com.epam.esm.model.User;
import com.epam.esm.repository.AbstractRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserRepository extends AbstractRepository<User> {

    protected UserRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<User> getEntityType() {
        return User.class;
    }
}
