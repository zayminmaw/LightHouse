package com.zayminmaw.lighthouse.repository;

import com.zayminmaw.lighthouse.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller,Long> {
    Seller findByUserId(long id);
}
