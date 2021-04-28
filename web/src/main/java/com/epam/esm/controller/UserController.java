package com.epam.esm.controller;

import com.epam.esm.converter.EntityConverter;
import com.epam.esm.dto.PurchaseDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.link.LinkBuilder;
import com.epam.esm.model.Purchase;
import com.epam.esm.model.User;
import com.epam.esm.service.PurchaseService;
import com.epam.esm.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final PurchaseService purchaseService;
    private final LinkBuilder<UserDto> userLinkBuilder;
    private final LinkBuilder<PurchaseDto> purchaseLinkBuilder;
    private final PagedResourcesAssembler<User> pagedUserResourcesAssembler;
    private final PagedResourcesAssembler<Purchase> pagePurchasedResourcesAssembler;
    private final RepresentationModelAssembler<Purchase, PurchaseDto> purchaseModelAssembler;
    private final RepresentationModelAssemblerSupport<User, UserDto> userAssembler;

    public UserController(UserService userService, PurchaseService purchaseService,
                          LinkBuilder<UserDto> userLinkBuilder,
                          LinkBuilder<PurchaseDto> purchaseLinkBuilder,
                          PagedResourcesAssembler<User> pagedUserResourcesAssembler,
                          PagedResourcesAssembler<Purchase> pagePurchasedResourcesAssembler,
                          RepresentationModelAssembler<Purchase, PurchaseDto> purchaseModelAssembler,
                          RepresentationModelAssemblerSupport<User, UserDto> userAssembler) {
        this.userService = userService;
        this.purchaseService = purchaseService;
        this.userLinkBuilder = userLinkBuilder;
        this.purchaseLinkBuilder = purchaseLinkBuilder;
        this.pagedUserResourcesAssembler = pagedUserResourcesAssembler;
        this.pagePurchasedResourcesAssembler = pagePurchasedResourcesAssembler;
        this.purchaseModelAssembler = purchaseModelAssembler;
        this.userAssembler = userAssembler;
    }

    @GetMapping()
    public ResponseEntity<PagedModel<UserDto>> findAll(Pageable pageable) {
        Page<User> users = userService.findAll(pageable);
        PagedModel<UserDto> dtos = pagedUserResourcesAssembler.toModel(users, userAssembler);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable Long id) {
        User user = userService.findById(id);
        UserDto dto = EntityConverter.map(user);
        return userLinkBuilder.buildLinks(dto);
    }

    @PostMapping("/{id}/purchases")
    public PurchaseDto addPurchase(@PathVariable Long id, @RequestBody PurchaseDto purchaseDto) {
        Purchase purchase = EntityConverter.map(purchaseDto);
        purchase = purchaseService.add(id, purchase);
        PurchaseDto dto = EntityConverter.map(purchase);
        return purchaseLinkBuilder.buildLinks(dto);
    }

    @GetMapping(value = "/{id}/purchases")
    public ResponseEntity<PagedModel<PurchaseDto>> findUserPurchases(@PathVariable Long id, Pageable pageable) {
        Page<Purchase> orders = purchaseService.findByUserId(id, pageable);
        PagedModel<PurchaseDto> dtos = pagePurchasedResourcesAssembler.toModel(orders, purchaseModelAssembler);
        return ResponseEntity.ok(dtos);
    }
}
