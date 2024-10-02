package com.defectus.smartrecruiter.service.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String identifier;  // Can be either username or email
    private String password;
}