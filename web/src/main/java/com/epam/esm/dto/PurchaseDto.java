package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Relation(collectionRelation = "purchases")
public class PurchaseDto extends RepresentationModel<PurchaseDto> {
    private Long id;
    private Long idUser;
    private int cost;
    private List<PurchasesCertificateDto> certificates;
    private LocalDateTime createTime;

    public PurchaseDto(Long id, Long idUser, int cost, List<PurchasesCertificateDto> certificates, LocalDateTime createTime) {
        this.id = id;
        this.idUser = idUser;
        this.cost = cost;
        this.createTime = createTime;
        this.certificates = certificates == null
                ? new ArrayList<>()
                : certificates;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public List<PurchasesCertificateDto> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<PurchasesCertificateDto> certificates) {
        this.certificates = certificates;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PurchaseDto.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("idUser=" + idUser)
                .add("cost=" + cost)
                .add("certificates=" + certificates)
                .add("createTime=" + createTime)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PurchaseDto purchaseDto = (PurchaseDto) o;
        return cost == purchaseDto.cost
                && Objects.equals(id, purchaseDto.id)
                && Objects.equals(idUser, purchaseDto.idUser)
                && Objects.equals(certificates, purchaseDto.certificates)
                && Objects.equals(createTime, purchaseDto.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, idUser, cost, certificates, createTime);
    }
}
