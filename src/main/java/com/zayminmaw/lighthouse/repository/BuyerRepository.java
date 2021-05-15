package com.zayminmaw.lighthouse.repository;

import com.zayminmaw.lighthouse.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerRepository extends JpaRepository<Buyer,Long> {
    Buyer findByUserId(long id);
}
