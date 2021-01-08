package com.tmd.lighthouse.entity.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderCreateRequest {
    private String address;
    private String phoneNo;
    private List<OrderItemRequest> product;
}
