package com.tmd.lighthouse.entity.response;

import com.tmd.lighthouse.entity.Product;
import lombok.Data;

import java.util.List;

@Data
public class ProductsCreateResponse extends Response{
    List<Product> newProducts;
}
