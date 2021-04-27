package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class PurchasesDto extends RepresentationModel<PurchasesDto> {
    private Long id;
    private Long idUser;
    private int cost;
    private List<PurchasesCertificateDto> certificates;
    private LocalDateTime createTime;

    public PurchasesDto(Long id, Long idUser, int cost, List<PurchasesCertificateDto> certificates, LocalDateTime createTime) {
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
        return new StringJoiner(", ", PurchasesDto.class.getSimpleName() + "[", "]")
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
        PurchasesDto purchasesDto = (PurchasesDto) o;
        return cost == purchasesDto.cost
                && Objects.equals(id, purchasesDto.id)
                && Objects.equals(idUser, purchasesDto.idUser)
                && Objects.equals(certificates, purchasesDto.certificates)
                && Objects.equals(createTime, purchasesDto.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, idUser, cost, certificates, createTime);
    }
}
