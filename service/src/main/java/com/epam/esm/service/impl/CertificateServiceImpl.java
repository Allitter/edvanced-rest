package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.specification.Specification;
import com.epam.esm.repository.specification.impl.certificate.*;
import com.epam.esm.repository.specification.impl.common.AllSpecification;
import com.epam.esm.repository.specification.impl.common.ModelByIdSpecification;
import com.epam.esm.repository.specification.impl.common.ModelNotRemovedSpecification;
import com.epam.esm.repository.specification.impl.tag.TagByNameSpecification;
import com.epam.esm.service.CertificateQueryObject;
import com.epam.esm.service.CertificateService;
import com.epam.esm.validator.CertificateValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class CertificateServiceImpl implements CertificateService {
    private final MainRepository<Tag> tagRepository;
    private final MainRepository<Certificate> repository;
    private final CertificateValidator validator;

    public CertificateServiceImpl(MainRepository<Certificate> repository,
                                  MainRepository<Tag> tagRepository,
                                  CertificateValidator validator) {
        this.repository = repository;
        this.tagRepository = tagRepository;
        this.validator = validator;
    }

    @Override
    public Certificate findById(long id) {
        Specification<Certificate> specification
                = Specification.of(new ModelByIdSpecification<>(id), new ModelNotRemovedSpecification<>());
        Optional<Certificate> optionalCertificate = repository.queryFirst(specification);
        return optionalCertificate.orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Certificate update(Certificate certificate) {
        throwExceptionIfNotValid(validator.validateForUpdate(certificate));

        Optional<Certificate> optional = repository.queryFirst(new ModelByIdSpecification<>(certificate.getId()));
        Certificate oldCertificate = optional.orElseThrow(EntityNotFoundException::new);

        Certificate certificateToUpdate = Certificate.Builder.merge(oldCertificate, certificate);
        List<Tag> ensuredTags = ensureTagsInRepo(certificateToUpdate.getTags());

        certificateToUpdate = new Certificate.Builder(certificateToUpdate)
                .setLastUpdateDate(LocalDate.now())
                .setTags(ensuredTags)
                .build();

        return repository.update(certificateToUpdate);
    }

    @Override
    public Certificate add(Certificate certificate) {
        throwExceptionIfNotValid(validator.validateForCreate(certificate));

        List<Tag> tags = CollectionUtils.isNotEmpty(certificate.getTags())
                ? ensureTagsInRepo(certificate.getTags())
                : Collections.emptyList();

        certificate = new Certificate.Builder(certificate)
                .setCreateDate(LocalDate.now())
                .setLastUpdateDate(LocalDate.now())
                .setTags(tags)
                .build();

        return repository.add(certificate);
    }

    private void throwExceptionIfNotValid(Map<String, String> validationFails) {
        if (MapUtils.isNotEmpty(validationFails)) {
            throw new ValidationException(validationFails);
        }
    }

    private List<Tag> ensureTagsInRepo(List<Tag> tags) {
        List<Tag> ensured = new ArrayList<>();

        tags.forEach(tag -> {
            Specification<Tag> specification = new TagByNameSpecification(tag.getName());
            Optional<Tag> optional = tagRepository.queryFirst(specification);
            tag = optional.isPresent() ? optional.get() : tagRepository.add(tag);
            ensured.add(tag);
        });

        return ensured;
    }

    @Override
    @Transactional
    public Certificate remove(long id) {
        Certificate certificate = repository.queryFirst(new ModelByIdSpecification<>(id))
                .orElseThrow(EntityNotFoundException::new);
        certificate.setRemoved(true);
        return certificate;
    }

    @Override
    public Page<Certificate> findCertificatesByQueryObject(CertificateQueryObject queryObject, Pageable pageable) {
        List<Specification<Certificate>> specifications = new ArrayList<>();

        if (StringUtils.isNotBlank(queryObject.getName())) {
            specifications.add(new CertificateByNameSpecification(queryObject.getName()));
        }
        if (StringUtils.isNotBlank(queryObject.getDescription())) {
            specifications.add(new CertificateByDescriptionSpecification(queryObject.getDescription()));
        }
        if (CollectionUtils.isNotEmpty(queryObject.getTagNames())) {
            queryObject.getTagNames()
                    .forEach(tagName -> specifications.add(new CertificateByTagNameSpecification(tagName)));
        }
        if (specifications.isEmpty()) {
            specifications.add(new AllSpecification<>());
        }
        specifications.add(new ModelNotRemovedSpecification<>());

        return repository.query(specifications, pageable);
    }
}
