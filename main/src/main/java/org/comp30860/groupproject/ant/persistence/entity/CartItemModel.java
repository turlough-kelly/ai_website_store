package org.comp30860.groupproject.ant.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
public class CartItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn
    private AccountModel account;

    @ManyToOne
    @JoinColumn
    private ProductModel product;

    @Column(nullable = false)
    private boolean trained;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private double price;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public CartItemModel() {
    }

    public CartItemModel(
            AccountModel account,
            ProductModel product,
            boolean trained,
            int amount) {
        this.account = account;
        this.product = product;
        this.trained = trained;
        this.amount = amount;
        if (trained) {
            this.price = product.getTrainedPrice() * amount;
        } else {
            this.price = product.getUntrainedPrice() * amount;
        }
    }

    @Override
    public String toString() {
        return String.format(
                "CartItem[id=%d, account id='%d', product id='%d', trained='%b', amount='%d', price='%f']",
                this.id, this.account.getId(), this.product.getId(), this.trained, this.amount, this.price);
    }

    public long getId() {
        return this.id;
    }

    public AccountModel getAccount() {
        return this.account;
    }

    public ProductModel getProduct() {
        return this.product;
    }

    public boolean isTrained() {
        return this.trained;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        this.updatedAt = LocalDateTime.now();
    }

    public void setTrained(boolean trained) {
        this.trained = trained;
        this.updatedAt = LocalDateTime.now();
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

}