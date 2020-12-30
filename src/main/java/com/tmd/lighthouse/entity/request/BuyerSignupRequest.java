package com.tmd.lighthouse.entity.request;

import com.tmd.lighthouse.entity.Buyer;
import com.tmd.lighthouse.entity.UserEntity;
import lombok.Data;

@Data
public class BuyerSignupRequest {
    private String email;
    private String password;
    private String name;
    private String address;
    private String phoneNo;
}
