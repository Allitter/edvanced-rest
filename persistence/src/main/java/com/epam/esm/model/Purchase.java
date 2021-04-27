package com.epam.esm.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "purchase")
public class Purchase implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cost")
    private int cost;
    @Column(name = "create_time")
    private LocalDateTime createTime;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.PERSIST)
    private List<PurchaseCertificate> purchaseCertificates;
    @Column(name = "removed", columnDefinition = "boolean default false")
    private boolean removed;

    public Purchase() {
    }

    public Purchase(Long id, int cost, LocalDateTime createTime, List<PurchaseCertificate> purchaseCertificates) {
        this.id = id;
        this.cost = cost;
        this.createTime = createTime;
        this.purchaseCertificates = purchaseCertificates;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PurchaseCertificate> getPurchaseCertificates() {
        return purchaseCertificates;
    }

    public void setPurchaseCertificates(List<PurchaseCertificate> purchaseCertificates) {
        this.purchaseCertificates = purchaseCertificates;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Purchase.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("cost=" + cost)
                .add("createTime=" + createTime)
                .add("user=" + user)
                .add("purchaseCertificates=" + purchaseCertificates)
                .add("removed=" + removed)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase purchase = (Purchase) o;
        return cost == purchase.cost
                && removed == purchase.removed
                && Objects.equals(id, purchase.id)
                && Objects.equals(createTime, purchase.createTime)
                && Objects.equals(user, purchase.user)
                && Objects.equals(purchaseCertificates, purchase.purchaseCertificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cost, createTime, user, purchaseCertificates, removed);
    }
}
