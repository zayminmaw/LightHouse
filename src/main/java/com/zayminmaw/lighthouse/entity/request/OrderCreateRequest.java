package com.zayminmaw.lighthouse.entity.request;

import lombok.Data;

@Data
public class OrderCreateRequest {
    private String address;
    private String phoneNo;
}
