package com.zayminmaw.lighthouse.entity.response;

import com.zayminmaw.lighthouse.entity.Product;
import lombok.Data;

@Data
public class ProductCreateResponse extends Response{
    Product newProduct;
}
