package com.tmd.lighthouse.entity.response;

import com.tmd.lighthouse.entity.Product;
import lombok.Data;

@Data
public class ProductUpdateResponse extends Response{
    Product updatedProduct;
}
