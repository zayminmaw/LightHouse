package com.tmd.lighthouse.entity.response;

import com.tmd.lighthouse.entity.Buyer;
import com.tmd.lighthouse.entity.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class BuyerUpdateResponse extends Response{
    private Buyer updatedBuyerUser;
}
