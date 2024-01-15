package org.comp30860.groupproject.ant.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class OrderModel {

    public OrderModel() {
    }

    public enum Status {
        PENDING,
        CONFIRMED,
        SHIPPED,
        CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn
    private AccountModel account;

    @Column(nullable = false)
    private double total;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Status status;

    @OneToMany(mappedBy = "order")
    private Set<OrderItemModel> orderItems = new HashSet<OrderItemModel>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public OrderModel(AccountModel account, Set<CartItemModel> cartItems) {
        this(account, cartItems, Status.PENDING);
    }

    public OrderModel(
            AccountModel account,
            Set<CartItemModel> cartItems,
            Status status) {
        this.account = account;
        for (CartItemModel item : cartItems) {
            if (item.getAmount() > 0) {
                this.orderItems.add(new OrderItemModel(this, item));
                this.total += item.getPrice();
            }
        }
        this.status = status;
    }

    public OrderModel(
            AccountModel account,
            Set<CartItemModel> cartItems,
            String status) {
        this.account = account;
        for (CartItemModel item : cartItems) {
            if (item.getAmount() > 0) {
                this.orderItems.add(new OrderItemModel(this, item));
                this.total += item.getPrice();
            }
        }
        this.status = Status.valueOf(status.toUpperCase());
    }

    @Override
    public String toString() {
        String str = String.format(
                "Order[id=%d, account id='%d', (",
                this.id, this.account.getId());
        for (OrderItemModel item : this.orderItems) {
            str += item.toString() + ", ";
        }
        str = str.substring(0, str.length() - 2);
        str += String.format(
                "), status='%s', total price='%f']",
                this.status, this.total);
        return str;
    }

    public long getId() {
        return this.id;
    }

    public AccountModel getAccount() {
        return this.account;
    }

    public double getTotal() {
        return this.total;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public Set<OrderItemModel> getOrderItems() {
        return this.orderItems;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

}