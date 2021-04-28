package com.epam.esm.repository.impl;

import com.epam.esm.model.User;
import com.epam.esm.repository.AbstractRepository;
import com.epam.esm.repository.specification.common.ModelByIdSpecification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class UserRepository extends AbstractRepository<User> {

    protected UserRepository(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<User> getEntityType() {
        return User.class;
    }

    @Override
    public Optional<User> remove(long id) {
        Optional<User> userOptional = queryFirst(new ModelByIdSpecification<>(id));
        userOptional.ifPresent(user -> user.setRemoved(true));
        return userOptional;
    }
}
