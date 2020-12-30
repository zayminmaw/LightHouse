package com.tmd.lighthouse.entity.response;

import com.tmd.lighthouse.entity.Seller;
import lombok.Data;

@Data
public class SellerUpdateResponse extends Response{
    private Seller updatedSellerUser;
}
