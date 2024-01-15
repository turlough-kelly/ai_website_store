package org.comp30860.groupproject.ant.persistence.repository;

import org.comp30860.groupproject.ant.persistence.entity.OrderItemModel;
import org.comp30860.groupproject.ant.persistence.entity.OrderModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemModel, Long> {

    public List<OrderItemModel> findByOrder(OrderModel order);

    public List<OrderItemModel> findByOrder(OrderModel order, Sort sort);

    public Page<OrderItemModel> findByOrder(OrderModel order, Pageable pageable);

}