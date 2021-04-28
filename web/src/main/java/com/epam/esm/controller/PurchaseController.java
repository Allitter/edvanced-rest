package com.epam.esm.controller;

import com.epam.esm.converter.EntityConverter;
import com.epam.esm.dto.PurchaseDto;
import com.epam.esm.link.LinkBuilder;
import com.epam.esm.model.Purchase;
import com.epam.esm.service.PurchaseService;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final LinkBuilder<PurchaseDto> orderLinkBuilder;

    public PurchaseController(PurchaseService purchaseService,
                              LinkBuilder<PurchaseDto> orderLinkBuilder) {
        this.purchaseService = purchaseService;
        this.orderLinkBuilder = orderLinkBuilder;
    }

    @GetMapping("/purchases/{id}")
    public PurchaseDto findById(@PathVariable Long id) {
        Purchase purchase = purchaseService.findById(id);
        PurchaseDto dto = EntityConverter.map(purchase);
        return orderLinkBuilder.buildLinks(dto);
    }
}
