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
@Table(name = "reviews")
public class ReviewModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn
    private AccountModel account;

    @ManyToOne
    @JoinColumn
    private ProductModel product;

    @Column(nullable = false, length = 1023)
    private String content;

    @Column(nullable = false)
    private int rating;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public ReviewModel() {
    }

    public ReviewModel(
            AccountModel account,
            ProductModel product,
            String content,
            int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        this.account = account;
        this.product = product;
        this.content = content;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return String.format(
                "Review[id=%d, account id='%d', product id='%d', rating='%d']",
                this.id, this.account.getId(), this.product.getId(), this.rating);
    }

    public long getId() {
        return this.id;
    }

    public AccountModel getAccount() {
        return this.account;
    }

    public void setAccount(AccountModel account) {
        this.account = account;
        this.updatedAt = LocalDateTime.now();
    }

    public ProductModel getProduct() {
        return this.product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
        this.updatedAt = LocalDateTime.now();
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

}