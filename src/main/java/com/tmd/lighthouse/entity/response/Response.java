package com.tmd.lighthouse.entity.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class Response  {
    private String statusCode;
    private String message;
    private String appVerion = "1.0";
}