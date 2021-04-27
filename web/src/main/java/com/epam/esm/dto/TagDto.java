package com.epam.esm.dto;

import com.epam.esm.util.ResourceBundleMessage;
import com.epam.esm.validation.ValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;
import java.util.StringJoiner;

public class TagDto extends RepresentationModel<TagDto> {
    private long id;
    @NotBlank(message = ResourceBundleMessage.TAG_NAME_EMPTY, groups = ValidationGroup.Create.class)
    @Pattern(regexp = "[\\w]{2,64}", message = ResourceBundleMessage.TAG_NAME_FORMAT)
    private String name;

    public TagDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
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
        return new StringJoiner(", ", TagDto.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TagDto tagDto = (TagDto) o;
        return id == tagDto.id && Objects.equals(name, tagDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
