package com.tmd.lighthouse.entity.request;

import lombok.Data;

@Data
public class SellerSignupRequest {
    private String email;
    private String password;
    private String name;
}
