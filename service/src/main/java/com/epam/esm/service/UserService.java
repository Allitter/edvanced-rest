package com.epam.esm.service;

import com.epam.esm.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    User findById(Long idUser);

    Page<User> findAll(Pageable pageable);

}
