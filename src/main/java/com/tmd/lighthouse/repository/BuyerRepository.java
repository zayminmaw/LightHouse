package com.tmd.lighthouse.repository;

import com.tmd.lighthouse.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerRepository extends JpaRepository<Buyer,Long> {
    Buyer findByUserId(long id);
}
