package com.zayminmaw.lighthouse.repository;


import com.zayminmaw.lighthouse.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findByOrderStatusAndBuyerId(String cart, long id);

    Order findByOrderStatusAndId(String confirm, long orderId);

    List<Order> findAllByOrderStatusNot(String cart);
}
