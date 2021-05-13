package com.epam.esm.service;

import com.epam.esm.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User findById(Long idUser);

    Page<User> findAll(Pageable pageable);

}
