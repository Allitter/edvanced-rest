package com.epam.esm.service;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class CertificateQueryObject {
    private String name;
    @JsonAlias({"tagName"})
    private List<String> tagNames;
    private String description;

    public CertificateQueryObject(String name, List<String> tagName,
                                  String description) {
        this.name = name;
        this.tagNames = tagName;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagName) {
        this.tagNames = tagName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CertificateQueryObject.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("tagNames=" + tagNames)
                .add("description='" + description + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CertificateQueryObject that = (CertificateQueryObject) o;
        return Objects.equals(name, that.name)
                && Objects.equals(tagNames, that.tagNames)
                && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, tagNames, description);
    }
}
