package com.zayminmaw.lighthouse.entity.response;

import com.zayminmaw.lighthouse.entity.Product;
import lombok.Data;

import java.util.List;

@Data
public class ProductsCreateResponse extends Response{
    List<Product> newProducts;
}
