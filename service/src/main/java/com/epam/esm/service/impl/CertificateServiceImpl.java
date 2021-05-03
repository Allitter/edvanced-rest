package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.MainRepository;
import com.epam.esm.repository.specification.certificate.CertificateByDescriptionSpecification;
import com.epam.esm.repository.specification.certificate.CertificateByNameSpecification;
import com.epam.esm.repository.specification.certificate.CertificateByTagNameSpecification;
import com.epam.esm.repository.specification.common.ModelByIdSpecification;
import com.epam.esm.repository.specification.common.ModelNotRemovedSpecification;
import com.epam.esm.repository.specification.tag.TagNameInSpecification;
import com.epam.esm.service.CertificateQueryObject;
import com.epam.esm.service.CertificateService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class CertificateServiceImpl implements CertificateService {
    private final MainRepository<Tag> tagRepository;
    private final MainRepository<Certificate> repository;

    public CertificateServiceImpl(MainRepository<Certificate> repository,
                                  MainRepository<Tag> tagRepository) {
        this.repository = repository;
        this.tagRepository = tagRepository;
    }

    @Override
    public Certificate findById(long id) {
        Specification<Certificate> specification = new ModelByIdSpecification<Certificate>(id)
                .and(new ModelNotRemovedSpecification<>());
        Optional<Certificate> optionalCertificate = repository.queryFirst(specification);
        return optionalCertificate.orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Certificate update(Certificate certificate) {
        Specification<Certificate> specification = new ModelByIdSpecification<>(certificate.getId());
        Optional<Certificate> optional = repository.queryFirst(specification);
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

    private List<Tag> ensureTagsInRepo(List<Tag> tags) {
        Set<String> tagNames = tags.stream().map(Tag::getName).collect(Collectors.toSet());
        List<Tag> tagsInRepo = tagRepository.queryList(new TagNameInSpecification(tagNames), Pageable.unpaged());
        tagsInRepo.forEach(tagInRepo -> tagNames.remove(tagInRepo.getName()));
        List<Tag> ensuredTags = new ArrayList<>(tagsInRepo);
        tags.stream().distinct()
                .filter(tag -> tagNames.contains(tag.getName()))
                .map(tagRepository::add)
                .forEach(ensuredTags::add);

        return ensuredTags;
    }

    @Override
    public Certificate remove(long id) {
        return repository.remove(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Page<Certificate> findCertificatesByQueryObject(CertificateQueryObject queryObject,
                                                           Pageable pageable, boolean eager) {

        Specification<Certificate> specification = new ModelNotRemovedSpecification<>();
        if (StringUtils.isNotBlank(queryObject.getName())) {
            specification = specification.and(new CertificateByNameSpecification(queryObject.getName()));
        }
        if (StringUtils.isNotBlank(queryObject.getDescription())) {
            specification = specification.and(new CertificateByDescriptionSpecification(queryObject.getDescription()));
        }
        if (CollectionUtils.isNotEmpty(queryObject.getTagNames())) {
            List<Specification<Certificate>> tagNameSpecifications = queryObject.getTagNames()
                    .stream().distinct()
                    .map(CertificateByTagNameSpecification::new)
                    .collect(Collectors.toList());
            for (Specification<Certificate> tagSpecification : tagNameSpecifications) {
                specification = specification.and(tagSpecification);
            }
        }

        return repository.query(specification, pageable, eager);
    }
}
