package com.epam.esm.model;


import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

import static java.util.Objects.hash;


@Entity
@Table(name = "certificate")
public class Certificate implements Model {
    @Id
    @SequenceGenerator(name="pk_sequence",sequenceName="certificate_id_seq", allocationSize=5)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
    @Column(name="id", unique=true, nullable=false)
    private Long id;
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    @Column(name = "description", length = 255, nullable = false)
    private String description;
    @Column(name = "price", nullable = false)
    private Integer price;
    @Column(name = "duration", nullable = false)
    private Integer duration;
    @Column(name = "create_date", nullable = false,  updatable = false)
    private LocalDate createDate;
    @Column(name = "last_update_date", nullable = false)
    private LocalDate lastUpdateDate;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "certificate_tag",
            joinColumns = {@JoinColumn(name = "id_certificate")},
            inverseJoinColumns = {@JoinColumn(name = "id_tag")}
    )
    private List<Tag> tags;
    @Column(name = "removed", columnDefinition = "boolean DEFAULT FALSE")
    private boolean removed;

    public Certificate() {
        this.tags = new ArrayList<>();
    }

    private Certificate(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.price = builder.price;
        this.duration = builder.duration;
        this.createDate = builder.createDate;
        this.lastUpdateDate = builder.lastUpdateDate;
        this.tags = Collections.unmodifiableList(builder.tags);
        this.removed = builder.removed;
    }

    @Override
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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        if (tags != null) {
            this.tags = tags;
        }
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certificate that = (Certificate) o;
        return Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(price, that.price)
                && Objects.equals(duration, that.duration)
                && Objects.equals(createDate, that.createDate)
                && Objects.equals(lastUpdateDate, that.lastUpdateDate)
                && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return hash(name, description, price, duration, createDate, lastUpdateDate, tags);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Certificate.class.getSimpleName() + "[", "]")
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

    public static class Builder {
        private static final int ZERO = 0;
        private Long id;
        private String name;
        private String description;
        private Integer price;
        private Integer duration;
        private LocalDate createDate;
        private LocalDate lastUpdateDate;
        private List<Tag> tags;
        private boolean removed = false;

        public Builder() {
            this.tags = new ArrayList<>();
        }

        public Builder(Certificate certificate) {
            this.id = certificate.getId();
            this.name = certificate.getName();
            this.description = certificate.getDescription();
            this.price = certificate.getPrice();
            this.duration = certificate.getDuration();
            this.createDate = certificate.getCreateDate();
            this.lastUpdateDate = certificate.getLastUpdateDate();
            this.tags = certificate.getTags();
        }

        public static Certificate merge(Certificate to, Certificate from) {
            Builder builder = new Builder(to);
            if (StringUtils.isNotBlank(from.name)) {
                builder.setName(from.name);
            }
            if (StringUtils.isNotBlank(from.description)) {
                builder.setDescription(from.description);
            }
            if (Objects.nonNull(from.price)) {
                builder.setPrice(from.price);
            }
            if (Objects.nonNull(from.duration) && from.duration != ZERO) {
                builder.setDuration(from.duration);
            }
            if (CollectionUtils.isNotEmpty(from.tags)) {
                builder.setTags(from.tags);
            }

            return builder.build();
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setPrice(Integer price) {
            this.price = price;
            return this;
        }

        public Builder setDuration(Integer duration) {
            this.duration = duration;
            return this;
        }

        public Builder setCreateDate(LocalDate createDate) {
            this.createDate = createDate;
            return this;
        }

        public Builder setLastUpdateDate(LocalDate lastUpdateDate) {
            this.lastUpdateDate = lastUpdateDate;
            return this;
        }

        public Builder setTags(List<Tag> tags) {
            if (tags != null) {
                this.tags = tags;
            }
            return this;
        }

        public Builder setRemoved(boolean removed) {
            return this;
        }

        public Certificate build() {
            return new Certificate(this);
        }
    }
}
