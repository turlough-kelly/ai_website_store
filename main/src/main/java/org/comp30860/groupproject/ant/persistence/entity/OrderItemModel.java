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
@Table(name = "order_items")
public class OrderItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn
    private OrderModel order;

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

    protected OrderItemModel() {
    }

    protected OrderItemModel(OrderModel order, CartItemModel item) {
        this(order, item.getProduct(), item.isTrained(), item.getAmount());
    }

    protected OrderItemModel(
            OrderModel order,
            ProductModel product,
            boolean trained,
            int amount) {
        this.order = order;
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
                "OrderItem[id=%d, product id='%d', trained='%b', amount='%f', price='%f']",
                this.id, this.product.getId(), this.trained, this.amount, this.price);
    }

    public long getId() {
        return this.id;
    }

    public OrderModel getOrder() {
        return this.order;
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

    public double getPrice() {
        return this.price;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

}