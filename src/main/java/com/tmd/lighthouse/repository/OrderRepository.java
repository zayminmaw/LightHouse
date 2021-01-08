package com.tmd.lighthouse.repository;


import com.tmd.lighthouse.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
