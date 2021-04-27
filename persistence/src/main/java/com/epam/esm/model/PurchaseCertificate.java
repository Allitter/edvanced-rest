package com.epam.esm.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
public class PurchaseCertificate implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_certificate")
    private Certificate certificate;
    @ManyToOne
    @JoinColumn(name = "id_purchase")
    private Purchase purchase;
    @Column(name = "cnt")
    private int count;

    public PurchaseCertificate() {
    }

    public PurchaseCertificate(Long id, Certificate certificate, Purchase purchase, int count) {
        this.id = id;
        this.certificate = certificate;
        this.purchase = purchase;
        this.count = count;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PurchaseCertificate.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("certificate=" + certificate)
                .add("purchase=" + purchase)
                .add("count=" + count)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseCertificate that = (PurchaseCertificate) o;
        return count == that.count
                && Objects.equals(id, that.id)
                && Objects.equals(certificate, that.certificate)
                && Objects.equals(purchase, that.purchase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, certificate, purchase, count);
    }
}
