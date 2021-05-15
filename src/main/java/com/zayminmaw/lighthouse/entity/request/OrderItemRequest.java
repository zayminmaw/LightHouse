package com.zayminmaw.lighthouse.entity.request;

import lombok.Data;

@Data
public class OrderItemRequest {
    private int quantity;
    private long id;
}
