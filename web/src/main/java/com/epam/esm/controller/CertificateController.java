package com.epam.esm.controller;

import com.epam.esm.converter.EntityConverter;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.link.LinkBuilder;
import com.epam.esm.link.assembler.CertificateNoTagsRepresentationalModelAssembler;
import com.epam.esm.link.assembler.CertificateRepresentationalModelAssembler;
import com.epam.esm.model.Certificate;
import com.epam.esm.service.CertificateQueryObject;
import com.epam.esm.service.CertificateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * The controller to provide CRUD operations on {@link Certificate}.
 */
@RestController
@RequestMapping(value = "/certificates")
public class CertificateController {
    private final CertificateService certificateService;
    private final LinkBuilder<CertificateDto> certificateLinkBuilder;
    private final PagedResourcesAssembler<Certificate> pagedResourcesAssembler;
    private final CertificateRepresentationalModelAssembler certificateAssembler;
    private final CertificateNoTagsRepresentationalModelAssembler certificateNoTagsAssembler;

    public CertificateController(CertificateService certificateService,
                                 LinkBuilder<CertificateDto> linkBuilder,
                                 PagedResourcesAssembler<Certificate> pagedResourcesAssembler,
                                 CertificateRepresentationalModelAssembler certificateAssembler,
                                 CertificateNoTagsRepresentationalModelAssembler certificateNoTagsAssembler) {
        this.certificateService = certificateService;
        this.certificateLinkBuilder = linkBuilder;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.certificateAssembler = certificateAssembler;
        this.certificateNoTagsAssembler = certificateNoTagsAssembler;
    }

    /**
     * Search for certificates by passed params.
     *
     * @param query Object containing all params
     * @return the list of queried certificates or all certificates if no params passed
     */
    @GetMapping()
    public ResponseEntity<PagedModel<CertificateDto>> findByQuery(CertificateQueryObject query,
                                                                  Pageable pageable,
                                                                  @RequestParam(defaultValue = "true") boolean fetchTags) {
        Page<Certificate> certificates = certificateService.findCertificatesByQueryObject(query, pageable);
        var assembler= fetchTags ? certificateAssembler : certificateNoTagsAssembler;
        PagedModel<CertificateDto> dtos = pagedResourcesAssembler.toModel(certificates, assembler);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Find by id.
     *
     * @param id the id of certificate
     * @return the {@link CertificateDto} of queried certificate
     */
    @GetMapping(value = "/{id}")
    public CertificateDto findById(@PathVariable long id) {
        Certificate certificate = certificateService.findById(id);
        CertificateDto dto = EntityConverter.map(certificate);
        return certificateLinkBuilder.buildLinks(dto);
    }

    /**
     * Add certificate.
     *
     * @param dto The certificate to add
     * @return the {@link CertificateDto} of added certificate
     */
    @PostMapping()
    public CertificateDto add(@Valid @RequestBody CertificateDto dto) {
        Certificate certificate = EntityConverter.map(dto);
        Certificate result = certificateService.add(certificate);
        CertificateDto certificateDto = EntityConverter.map(result);
        return certificateLinkBuilder.buildLinks(certificateDto);
    }

    /**
     * Update certificate.
     *
     * @param id  the id of certificate to update
     * @param dto the updated fields of certificate
     * @return the updated certificate {@link CertificateDto}
     */
    @PutMapping(value = "/{id}")
    public CertificateDto update(@PathVariable long id, @RequestBody CertificateDto dto) {
        dto.setId(id);
        Certificate certificate = EntityConverter.map(dto);
        Certificate updated = certificateService.update(certificate);
        CertificateDto certificateDto = EntityConverter.map(updated);
        return certificateLinkBuilder.buildLinks(certificateDto);
    }

    /**
     * Delete certificate.
     *
     * @param id the id of certificate
     * @return no content
     */
    @DeleteMapping(value = "/{id}")
    public CertificateDto remove(@PathVariable long id) {
        Certificate certificate = certificateService.remove(id);
        return EntityConverter.map(certificate);
    }
}
