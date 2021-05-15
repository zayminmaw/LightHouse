package com.zayminmaw.lighthouse.entity.response;

import com.zayminmaw.lighthouse.entity.Buyer;
import lombok.Data;

@Data
public class BuyerUpdateResponse extends Response{
    private Buyer updatedBuyerUser;
}
