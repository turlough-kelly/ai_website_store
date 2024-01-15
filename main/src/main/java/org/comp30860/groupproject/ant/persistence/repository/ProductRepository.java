package org.comp30860.groupproject.ant.persistence.repository;

import org.comp30860.groupproject.ant.persistence.entity.ProductModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductModel, Long> {

    public ProductModel findByName(String name);

    public List<ProductModel> findByHiddenFalse();

    public List<ProductModel> findByHiddenFalse(Sort sort);

    public Page<ProductModel> findByHiddenFalse(Pageable pageable);

}