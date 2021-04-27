package com.epam.esm.controller;

import com.epam.esm.converter.EntityConverter;
import com.epam.esm.dto.UserDto;
import com.epam.esm.link.LinkBuilder;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final LinkBuilder<UserDto> userLinkBuilder;
    private final PagedResourcesAssembler<User> pagedResourcesAssembler;
    private final RepresentationModelAssemblerSupport<User, UserDto> userAssembler;

    public UserController(UserService userService,
                          LinkBuilder<UserDto> userLinkBuilder,
                          PagedResourcesAssembler<User> pagedResourcesAssembler,
                          RepresentationModelAssemblerSupport<User, UserDto> userAssembler) {
        this.userService = userService;
        this.userLinkBuilder = userLinkBuilder;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.userAssembler = userAssembler;
    }

    @GetMapping()
    public ResponseEntity<PagedModel<UserDto>> findAll(Pageable pageable) {
        Page<User> users = userService.findAll(pageable);
        PagedModel<UserDto> dtos = pagedResourcesAssembler.toModel(users, userAssembler);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable Long id) {
        User user = userService.findById(id);
        UserDto dto = EntityConverter.map(user);
        return userLinkBuilder.buildLinks(dto);
    }
}
