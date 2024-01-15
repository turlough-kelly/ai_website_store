package org.comp30860.groupproject.ant.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "accounts")
public class AccountModel {

    public enum Role {
        OWNER,
        ADMIN,
        CUSTOMER;

        @Override
        public String toString() {
            return "ROLE_" + this.name();
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "email_address", nullable = false, unique = true)
    private String emailAddress;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Role role = Role.CUSTOMER;

    @OneToMany(mappedBy = "account")
    private Set<CartItemModel> cart = new HashSet<CartItemModel>();

    @OneToMany(mappedBy = "account")
    private Set<OrderModel> orders = new HashSet<OrderModel>();

    @OneToMany(mappedBy = "account")
    private Set<ReviewModel> reviews = new HashSet<ReviewModel>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Transient
    static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AccountModel() {
    }

    public AccountModel(
            String firstname,
            String surname,
            String username,
            String emailAddress,
            String password) {
        this(firstname, surname, username, emailAddress, password, Role.CUSTOMER);
    }

    public AccountModel(
            String firstname,
            String surname,
            String username,
            String emailAddress,
            String password,
            Role role) {
        this.firstname = firstname;
        this.surname = surname;
        this.username = username;
        this.emailAddress = emailAddress;
        this.setPassword(password);
        this.role = role;
    }

    public AccountModel(
            String firstname,
            String surname,
            String username,
            String emailAddress,
            String password,
            String role) {
        this.firstname = firstname;
        this.surname = surname;
        this.username = username;
        this.emailAddress = emailAddress;
        this.setPassword(password);
        this.role = Role.valueOf(role.toUpperCase());
    }

    @Override
    public String toString() {
        return String.format(
                "Account[id=%d, name='%s %s', username='%s', email address='%s', role='%s']",
                this.id, this.firstname, this.surname, this.username, this.emailAddress, this.role);
    }

    public long getId() {
        return this.id;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
        this.updatedAt = LocalDateTime.now();
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
        this.updatedAt = LocalDateTime.now();
    }

    public String getFullName() {
        return this.getFirstname() + " " + this.getSurname();
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
        this.updatedAt = LocalDateTime.now();
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        this.updatedAt = LocalDateTime.now();
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        String encodedPassword = passwordEncoder.encode(password);
        this.password = encodedPassword;
        this.updatedAt = LocalDateTime.now();
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
        this.updatedAt = LocalDateTime.now();
    }

    public Set<CartItemModel> getCart() {
        return this.cart;
    }

    public Double getCartPrice() {
        return this.cart.stream().mapToDouble(CartItemModel::getPrice).sum();
    }

    public Set<OrderModel> getOrders() {
        return this.orders;
    }

    public Set<ReviewModel> getReviews() {
        return this.reviews;
    }

    public void promote() {
        if (this.role == Role.CUSTOMER) {
            this.role = Role.ADMIN;
            this.updatedAt = LocalDateTime.now();
        } else if (this.role == Role.ADMIN) {
            this.role = Role.OWNER;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void demote() {
        if (this.role == Role.ADMIN) {
            this.role = Role.CUSTOMER;
            this.updatedAt = LocalDateTime.now();
        }
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

}