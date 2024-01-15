package org.comp30860.groupproject.ant.persistence.repository;

import org.comp30860.groupproject.ant.persistence.entity.OrderModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderModel, Long> {

    @Query("select o from OrderModel o where o.account.id = ?#{ principal?.id }")
    public List<OrderModel> findMyOrders();

    @Query("select o from OrderModel o where o.account.id = ?#{ principal?.id }")
    public List<OrderModel> findMyOrders(Sort sort);

    @Query("select o from OrderModel o where o.account.id = ?#{ principal?.id }")
    public Page<OrderModel> findMyOrders(Pageable pageable);

}