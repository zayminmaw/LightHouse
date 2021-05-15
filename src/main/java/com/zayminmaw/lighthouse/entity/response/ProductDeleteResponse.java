package com.zayminmaw.lighthouse.entity.response;

import com.zayminmaw.lighthouse.entity.Product;
import lombok.Data;

@Data
public class ProductDeleteResponse extends Response{
    Product deletedProduct;
}
