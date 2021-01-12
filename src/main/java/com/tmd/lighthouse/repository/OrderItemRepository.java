package com.tmd.lighthouse.repository;

import com.tmd.lighthouse.entity.OrderItem;
import com.tmd.lighthouse.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

    //Testing

//    @Modifying
//    @Query("delete from OrderItem o where o.orderId = ?1")
//    void deleteByOrderId(long id);

    Iterable<? extends OrderItem> findAllByOrderId(long id);

    @Query(value = "select o.product from OrderItem o where o.orderId = :orderId")
    List<Product> findAllProductByOrderId(@Param("orderId") long orderId);

    OrderItem findByProductIdAndOrderId(long productId, long id);

    @Query(value = "select "+"new com.tmd.lighthouse.entity.response.CartResponse(o.quantity,o.subTotal,o.product)"+" from OrderItem o where o.orderId = :orderId")
    List<Object> findAllProductAndQuantityAndSubTotalByOrderId(@Param("orderId") long orderId);
}
