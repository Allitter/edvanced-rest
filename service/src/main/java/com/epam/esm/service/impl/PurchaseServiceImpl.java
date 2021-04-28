package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Purchase;
import com.epam.esm.model.PurchaseCertificate;
import com.epam.esm.model.User;
import com.epam.esm.repository.impl.PurchaseRepository;
import com.epam.esm.repository.specification.common.ModelByIdSpecification;
import com.epam.esm.repository.specification.common.ModelNotRemovedSpecification;
import com.epam.esm.repository.specification.order.OrderByUserIdSpecification;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.PurchaseService;
import com.epam.esm.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    private final UserService userService;
    private final CertificateService certificateService;
    private final PurchaseRepository purchaseRepository;

    public PurchaseServiceImpl(UserService userService,
                               CertificateService certificateService,
                               PurchaseRepository purchaseRepository) {
        this.userService = userService;
        this.certificateService = certificateService;
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public Purchase findById(Long id) {
        return purchaseRepository
                .queryFirst(new ModelByIdSpecification<Purchase>(id).and(new ModelNotRemovedSpecification<>()))
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Page<Purchase> findByUserId(Long idUser, Pageable pageable) {
        Specification<Purchase> specification = new OrderByUserIdSpecification(idUser)
                .and(new ModelNotRemovedSpecification<>());

        return purchaseRepository.query(specification, pageable);
    }

    @Override
    @Transactional
    public Purchase add(Long idUser, Purchase purchase) {
        List<PurchaseCertificate> compressedPurchaseCertificates
                = compressPurchaseCertificates(purchase.getPurchaseCertificates());
        purchase.setPurchaseCertificates(compressedPurchaseCertificates);

        purchase.getPurchaseCertificates().forEach(purchaseCertificate -> {
            Long id = purchaseCertificate.getCertificate().getId();
            purchaseCertificate.setCertificate(certificateService.findById(id));
            purchaseCertificate.setPurchase(purchase);
        });

        int cost = purchase.getPurchaseCertificates().stream()
                .mapToInt(orderCertificate -> {
                    int price = orderCertificate.getCertificate().getPrice();
                    int count = orderCertificate.getCount();
                    return price * count;
                })
                .sum();

        User user = userService.findById(idUser);
        purchase.setUser(user);
        purchase.setCost(cost);
        purchase.setCreateTime(LocalDateTime.now());
        return purchaseRepository.add(purchase);
    }

    private List<PurchaseCertificate> compressPurchaseCertificates(List<PurchaseCertificate> purchaseCertificates) {
        Map<Long, PurchaseCertificate> purchaseCertificateMap = new HashMap<>(); // key is certificate id
        purchaseCertificates.forEach(purchaseCertificate -> putToMapOrCombineCount(purchaseCertificateMap, purchaseCertificate));
        return new ArrayList<>(purchaseCertificateMap.values());
    }

    // Certificate id is used as purchaseCertificateMap key
    private void putToMapOrCombineCount(Map<Long, PurchaseCertificate> purchaseCertificateMap,
                                        PurchaseCertificate purchaseCertificate) {

        Long certificateId = purchaseCertificate.getCertificate().getId();
        PurchaseCertificate previousValue = purchaseCertificateMap.put(certificateId, purchaseCertificate);
        if (previousValue != null) {
            int newCount = purchaseCertificate.getCount() + previousValue.getCount();
            purchaseCertificate.setCount(newCount);
        }
    }
}
