package com.tmd.lighthouse.entity.request;

import com.tmd.lighthouse.entity.response.Response;
import lombok.Data;

@Data
public class SellerUpdateRequest{
    private Long id;
    private String name;
}
