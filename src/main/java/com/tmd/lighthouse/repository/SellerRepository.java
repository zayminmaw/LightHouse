package com.tmd.lighthouse.repository;

import com.tmd.lighthouse.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller,Long> {
    Seller findByUserId(long id);
}
