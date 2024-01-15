package org.comp30860.groupproject.ant.persistence.repository;

import org.comp30860.groupproject.ant.persistence.entity.ProductModel;
import org.comp30860.groupproject.ant.persistence.entity.ReviewModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewModel, Long> {

    @Query("select r from ReviewModel r where r.account.id = ?#{ principal?.id }")
    public List<ReviewModel> findMyReviews();

    @Query("select r from ReviewModel r where r.account.id = ?#{ principal?.id }")
    public List<ReviewModel> findMyReviews(Sort sort);

    @Query("select r from ReviewModel r where r.account.id = ?#{ principal?.id }")
    public Page<ReviewModel> findMyReviews(Pageable pageable);

    public List<ReviewModel> findByProduct(ProductModel product);

    public List<ReviewModel> findByProduct(ProductModel product, Sort sort);

    public Page<ReviewModel> findByProduct(ProductModel product, Pageable pageable);

}