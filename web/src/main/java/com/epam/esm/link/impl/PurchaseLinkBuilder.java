package com.epam.esm.link.impl;

import com.epam.esm.controller.PurchaseController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.PurchaseDto;
import com.epam.esm.link.LinkBuilder;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PurchaseLinkBuilder implements LinkBuilder<PurchaseDto> {

    @Override
    public PurchaseDto buildLinks(PurchaseDto dto) {
        return dto.add(selfLink(dto))
                .add(linkToAll(dto.getIdUser()));
    }

    @Override
    public PurchaseDto buildLinksPaged(PurchaseDto dto) {
        return dto.add(selfLink(dto));
    }

    private Link selfLink(PurchaseDto dto) {
        return linkTo(methodOn(PurchaseController.class)
                .findById(dto.getId()))
                .withSelfRel();
    }

    private Link linkToAll(long idUser) {
        return linkTo(methodOn(UserController.class).findUserPurchases(idUser, null)).withRel("all");
    }

}
