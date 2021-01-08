package com.tmd.lighthouse.repository;

import com.tmd.lighthouse.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

    //Testing

//    @Modifying
//    @Query("delete from OrderItem o where o.orderId = ?1")
//    void deleteByOrderId(long id);

    Iterable<? extends OrderItem> findAllByOrderId(long id);
}
