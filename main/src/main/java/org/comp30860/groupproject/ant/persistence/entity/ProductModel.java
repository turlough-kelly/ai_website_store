package org.comp30860.groupproject.ant.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, length = 1023)
    private String description;

    @Column(name = "trained_price", nullable = false)
    private double trainedPrice;

    @Column(name = "untrained_price", nullable = false)
    private double untrainedPrice;

    @Column(nullable = false)
    private boolean hidden;

    @Column
    private String image;

    @OneToMany(mappedBy = "product")
    private Set<OrderItemModel> orderItems = new HashSet<OrderItemModel>();

    @OneToMany(mappedBy = "product")
    private Set<CartItemModel> cartItems = new HashSet<CartItemModel>();

    @OneToMany(mappedBy = "product")
    private Set<ReviewModel> reviews = new HashSet<ReviewModel>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private static String[] images;

    private static int imageCounter = 0;

    static {
        String value_name = System.getenv("SPRING_PROFILES_ACTIVE");
        File imageDir;
        if (value_name != null && value_name.equals("docker")) {
            imageDir = new File("/app/static/bot-images");
        } else{
            imageDir = new File("src/main/resources/static/bot-images");
        }
        images = imageDir.list();
        for (int i = 0; i < images.length; i++) {
            images[i] = images[i].replace("src/main/resources/static/bot-images/", "");
        }
    }

    private static String getInternalImage() {
        String image = images[imageCounter];
        imageCounter = (imageCounter + 1) % images.length;
        return image;
    }

    public ProductModel() {
        this.image = getInternalImage();
    }

    public ProductModel(
            String name,
            String description,
            double trainedPrice,
            double untrainedPrice) {
        this(name, description, trainedPrice, untrainedPrice, false);
    }

    public ProductModel(
            String name,
            String description,
            double trainedPrice,
            double untrainedPrice,
            boolean hidden) {
        this.name = name;
        this.description = description;
        this.trainedPrice = trainedPrice;
        this.untrainedPrice = untrainedPrice;
        this.hidden = hidden;
        this.image = getInternalImage();
    }

    @Override
    public String toString() {
        return String.format(
                "Product[id=%d, name='%s', trained price='%f', untrained price='%f']",
                this.id, this.name, this.trainedPrice, this.untrainedPrice);
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public double getTrainedPrice() {
        return this.trainedPrice;
    }

    public void setTrainedPrice(double trainedPrice) {
        this.trainedPrice = trainedPrice;
        this.updatedAt = LocalDateTime.now();
    }

    public double getUntrainedPrice() {
        return this.untrainedPrice;
    }

    public void setUntrainedPrice(double untrainedPrice) {
        this.untrainedPrice = untrainedPrice;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void hide() {
        this.hidden = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void show() {
        this.hidden = false;
        this.updatedAt = LocalDateTime.now();
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<ReviewModel> getReviews() {
        return this.reviews;
    }

    public double getRating() {
        double rating = 0;
        for (ReviewModel review : this.reviews) {
            rating += review.getRating();
        }
        return rating / this.reviews.size();
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void update(ProductModel newProduct) {
        String newDescription = newProduct.getDescription();
        if (newDescription != null && !newDescription.isEmpty()) {
            this.setDescription(newDescription);
        }

        double newTrainedPrice = newProduct.getTrainedPrice();
        if (newTrainedPrice > 0) {
            this.setTrainedPrice(newTrainedPrice);
        }

        double newUntrainedPrice = newProduct.getUntrainedPrice();
        if (newUntrainedPrice > 0) {
            this.setUntrainedPrice(newUntrainedPrice);
        }
    }

}