package com.tmd.lighthouse.entity.response;

import com.tmd.lighthouse.entity.Product;
import lombok.Data;

@Data
public class ProductCreateResponse extends Response{
    Product newProduct;
}
