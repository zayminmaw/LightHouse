package com.tmd.lighthouse.entity.response;

import com.tmd.lighthouse.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartResponse {
    private long quantity;
    private Double subTotal;
    private Product product;
}
