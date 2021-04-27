package com.epam.esm.controller;

import com.epam.esm.converter.EntityConverter;
import com.epam.esm.dto.PurchasesDto;
import com.epam.esm.link.LinkBuilder;
import com.epam.esm.model.Purchase;
import com.epam.esm.service.PurchaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final LinkBuilder<PurchasesDto> orderLinkBuilder;
    private final RepresentationModelAssembler<Purchase, PurchasesDto> purchaseModelAssembler;
    private final PagedResourcesAssembler<Purchase> pagedResourcesAssembler;

    public PurchaseController(PurchaseService purchaseService,
                              LinkBuilder<PurchasesDto> orderLinkBuilder,
                              RepresentationModelAssembler<Purchase, PurchasesDto> purchaseModelAssembler,
                              PagedResourcesAssembler<Purchase> pagedResourcesAssembler) {
        this.purchaseService = purchaseService;
        this.orderLinkBuilder = orderLinkBuilder;
        this.purchaseModelAssembler = purchaseModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping(value = "/users/{idUser}/purchases")
    public ResponseEntity<PagedModel<PurchasesDto>> findUserPurchases(@PathVariable Long idUser, Pageable pageable) {
        Page<Purchase> orders = purchaseService.findByUserId(idUser, pageable);
        PagedModel<PurchasesDto> dtos = pagedResourcesAssembler.toModel(orders, purchaseModelAssembler);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/purchases/{id}")
    public PurchasesDto findById(@PathVariable Long id) {
        Purchase purchase = purchaseService.findById(id);
        PurchasesDto dto = EntityConverter.map(purchase);
        return orderLinkBuilder.buildLinks(dto);
    }

    @PostMapping("/users/{idUser}")
    public PurchasesDto addPurchase(@PathVariable Long idUser, @RequestBody PurchasesDto purchasesDto) {
        Purchase purchase = EntityConverter.map(purchasesDto);
        purchase = purchaseService.add(idUser, purchase);
        PurchasesDto dto = EntityConverter.map(purchase);
        return orderLinkBuilder.buildLinks(dto);
    }
}
