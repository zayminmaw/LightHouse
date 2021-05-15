package com.zayminmaw.lighthouse.entity.response;

import com.zayminmaw.lighthouse.entity.Seller;
import lombok.Data;

@Data
public class SellerUpdateResponse extends Response{
    private Seller updatedSellerUser;
}
