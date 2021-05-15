package com.zayminmaw.lighthouse.entity.response;

import lombok.Data;

@Data
public class Response  {
    private String statusCode;
    private String message;
    private String appVerion = "1.0";
}