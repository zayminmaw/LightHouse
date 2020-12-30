package com.tmd.lighthouse.entity.request;

import lombok.Data;

@Data
public class BuyerUpdateRequest {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String address;
    private String phoneNo;
}
