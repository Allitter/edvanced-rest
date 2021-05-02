package com.epam.esm.converter;

import com.epam.esm.dto.*;
import com.epam.esm.model.*;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class EntityConverter {

    private EntityConverter() {
    }

    public static CertificateDto map(Certificate certificate) {
        List<TagDto> tagDtos = certificate.getTags().stream()
                .map(EntityConverter::map)
                .collect(Collectors.toList());

        return new CertificateDto(
                certificate.getId(),
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                certificate.getCreateDate(),
                certificate.getLastUpdateDate(),
                tagDtos);
    }

    public static CertificateDto mapCertificateNoTags(Certificate certificate) {
        return new CertificateDto(
                certificate.getId(),
                certificate.getName(),
                certificate.getDescription(),
                certificate.getPrice(),
                certificate.getDuration(),
                certificate.getCreateDate(),
                certificate.getLastUpdateDate(),
                Collections.emptyList());
    }

    public static Certificate map(CertificateDto dto) {
        List<Tag> tags = dto.getTags().stream()
                .map(EntityConverter::map)
                .collect(Collectors.toList());

        return new Certificate.Builder()
                .setId(dto.getId())
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setPrice(dto.getPrice())
                .setDuration(dto.getDuration())
                .setCreateDate(dto.getCreateDate())
                .setLastUpdateDate(dto.getLastUpdateDate())
                .setTags(tags)
                .build();
    }

    public static Tag map(TagDto dto) {
        return new Tag(dto.getId(), dto.getName());
    }

    public static TagDto map(Tag tag) {
        return new TagDto(tag.getId(), tag.getName());
    }

    public static PurchaseDto mapPurchaseNoCertificates(Purchase purchase) {
        return new PurchaseDto(
                purchase.getId(),
                purchase.getUser().getId(),
                purchase.getCost(),
                new ArrayList<>(),
                purchase.getCreateTime());
    }

    public static PurchaseDto map(Purchase purchase) {
        List<PurchasesCertificateDto> certificates = purchase.getPurchaseCertificates()
                .stream()
                .map(EntityConverter::map)
                .collect(Collectors.toList());

        return new PurchaseDto(
                purchase.getId(),
                purchase.getUser().getId(),
                purchase.getCost(),
                certificates,
                purchase.getCreateTime());
    }

    public static Purchase map(PurchaseDto purchaseDto) {
        List<PurchaseCertificate> purchaseCertificates = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(purchaseDto.getCertificates())) {
            purchaseCertificates = purchaseDto.getCertificates()
                    .stream()
                    .map(EntityConverter::map)
                    .collect(Collectors.toList());
        }

        return new Purchase(purchaseDto.getId(), purchaseDto.getCost(), purchaseDto.getCreateTime(), purchaseCertificates);
    }

    public static PurchasesCertificateDto map(PurchaseCertificate purchaseCertificate) {
        PurchasesCertificateDto purchasesCertificateDto = new PurchasesCertificateDto();
        purchasesCertificateDto.setCount(purchaseCertificate.getCount());
        Certificate certificate = purchaseCertificate.getCertificate();
        purchasesCertificateDto.setId(certificate.getId());
        purchasesCertificateDto.setCreateDate(certificate.getCreateDate());
        purchasesCertificateDto.setLastUpdateDate(certificate.getLastUpdateDate());
        purchasesCertificateDto.setName(certificate.getName());
        purchasesCertificateDto.setDescription(certificate.getDescription());
        purchasesCertificateDto.setDuration(certificate.getDuration());
        purchasesCertificateDto.setPrice(certificate.getPrice());

        List<TagDto> tagDtos = certificate.getTags().stream()
                .map(EntityConverter::map)
                .collect(Collectors.toList());
        purchasesCertificateDto.setTags(tagDtos);

        return purchasesCertificateDto;
    }

    public static PurchaseCertificate map(PurchasesCertificateDto dto) {
        PurchaseCertificate purchaseCertificate = new PurchaseCertificate();
        purchaseCertificate.setCount(dto.getCount());
        Certificate certificate =new Certificate.Builder()
                .setId(dto.getId())
                .build();
        purchaseCertificate.setCertificate(certificate);
        return purchaseCertificate;
    }

    public static UserDto map(User user) {
        return new UserDto(user.getId(),user.getLogin());
    }

    public static User map(UserDto userDto) {
        return new User(userDto.getId(), userDto.getLogin());
    }
}
