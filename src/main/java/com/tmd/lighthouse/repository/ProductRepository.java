package com.tmd.lighthouse.repository;

import com.tmd.lighthouse.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
