package com.epam.esm.link.impl;

import com.epam.esm.controller.PurchaseController;
import com.epam.esm.dto.PurchasesDto;
import com.epam.esm.link.LinkBuilder;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PurchaseLinkBuilder implements LinkBuilder<PurchasesDto> {

    @Override
    public PurchasesDto buildLinks(PurchasesDto dto) {
        return dto.add(selfLink(dto))
                .add(linkToAll(dto.getIdUser()));
    }

    @Override
    public PurchasesDto buildLinksPaged(PurchasesDto dto) {
        return dto.add(selfLink(dto));
    }

    private Link selfLink(PurchasesDto dto) {
        return linkTo(methodOn(PurchaseController.class)
                .findById(dto.getId()))
                .withSelfRel();
    }

    private Link linkToAll(long idUser) {
        return linkTo(methodOn(PurchaseController.class).findUserPurchases(idUser, null)).withRel("all");
    }

}
