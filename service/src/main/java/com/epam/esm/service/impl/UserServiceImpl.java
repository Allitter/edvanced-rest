package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.User;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.specification.common.AllSpecification;
import com.epam.esm.repository.specification.common.ModelByIdSpecification;
import com.epam.esm.repository.specification.common.ModelNotRemovedSpecification;
import com.epam.esm.repository.specification.user.UserByLoginSpecification;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class UserServiceImpl implements UserService {
    private final MainRepository<User> userRepository;

    @Override
    public User findById(Long id) {
        Specification<User> specification =
                new ModelByIdSpecification<User>(id)
                        .and(new ModelNotRemovedSpecification<>());

        return userRepository
                .queryFirst(specification)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.query(new AllSpecification<>(), pageable);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.queryFirst(new UserByLoginSpecification(username))
                .orElseThrow(EntityNotFoundException::new);
    }
}
