package com.tmd.lighthouse.repository;


import com.tmd.lighthouse.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findByOrderStatusAndBuyerId(String cart, long id);
}
