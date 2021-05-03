package com.epam.esm.dto;

import com.epam.esm.validation.ResourceBundleMessage;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

import static com.epam.esm.validation.ValidationGroup.Create;

@Relation(collectionRelation = "certificates")
public class CertificateDto extends RepresentationModel<CertificateDto> {
    private Long id;
    @NotBlank(message = ResourceBundleMessage.CERTIFICATE_NAME_EMPTY, groups = {Create.class})
    @Size(min = 2, max = 64, message = ResourceBundleMessage.CERTIFICATE_NAME_FORMAT)
    private String name;
    @NotBlank(message = ResourceBundleMessage.CERTIFICATE_DESCRIPTION_EMPTY, groups = {Create.class})
    @Size(min = 2, max = 255, message = ResourceBundleMessage.CERTIFICATE_DESCRIPTION_FORMAT)
    private String description;
    @NotNull(message = ResourceBundleMessage.CERTIFICATE_PRICE_EMPTY, groups = {Create.class})
    @Min(value = 0, message = ResourceBundleMessage.CERTIFICATE_PRICE_FORMAT)
    @Max(value = 999999, message = ResourceBundleMessage.CERTIFICATE_PRICE_FORMAT)
    private Integer price;
    @NotNull(message = ResourceBundleMessage.CERTIFICATE_DURATION_EMPTY, groups = {Create.class})
    @Min(value = 1, message = ResourceBundleMessage.CERTIFICATE_DURATION_FORMAT)
    @Max(value = Integer.MAX_VALUE, message = ResourceBundleMessage.CERTIFICATE_DURATION_FORMAT)
    private Integer duration;
    private LocalDate createDate;
    private LocalDate lastUpdateDate;
    @Valid
    private Set<TagDto> tags;

    public CertificateDto() {
        tags = new LinkedHashSet<>();
    }

    public CertificateDto(long id) {
        this.id = id;
    }

    public CertificateDto(Long id, String name, String description, Integer price, Integer duration,
                          LocalDate createDate, LocalDate lastUpdateDate, List<TagDto> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;

        if (tags != null) {
            this.tags = new LinkedHashSet<>(tags);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDate lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Set<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
        if (CollectionUtils.isNotEmpty(tags)) {
            this.tags = new LinkedHashSet<>(tags);
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CertificateDto.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .add("price=" + price)
                .add("duration=" + duration)
                .add("createDate=" + createDate)
                .add("lastUpdateDate=" + lastUpdateDate)
                .add("tags=" + tags)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CertificateDto that = (CertificateDto) o;
        return id.equals(that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(price, that.price)
                && Objects.equals(duration, that.duration)
                && Objects.equals(createDate, that.createDate)
                && Objects.equals(lastUpdateDate, that.lastUpdateDate)
                && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration, createDate, lastUpdateDate, tags);
    }
}
