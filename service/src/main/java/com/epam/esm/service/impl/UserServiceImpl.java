package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.User;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.specification.Specification;
import com.epam.esm.repository.specification.impl.common.AllSpecification;
import com.epam.esm.repository.specification.impl.common.ModelByIdSpecification;
import com.epam.esm.repository.specification.impl.common.ModelNotRemovedSpecification;
import com.epam.esm.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final MainRepository<User> userRepository;

    public UserServiceImpl(MainRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(Long id) {
        return userRepository
                .queryFirst(Specification.of(new ModelByIdSpecification<>(id),
                                             new ModelNotRemovedSpecification<>()))
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.query(new AllSpecification<>(), pageable);
    }
}
