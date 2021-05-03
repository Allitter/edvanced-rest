package com.epam.esm.model;

import com.epam.esm.audit.EntityActionListener;

import javax.persistence.*;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@EntityListeners(EntityActionListener.class)
@Table(name = "tag")
public class Tag implements Model {
    private static final int HASH_CODE = 61;

    @Id
    @SequenceGenerator(name = "tag_id_seq", sequenceName = "tag_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_id_seq")
    private Long id;
    @Column(name = "name", length = 64, nullable = false, unique = true)
    private String name;

    public Tag() {
    }

    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Tag.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id)
                && Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return HASH_CODE;
    }
}
