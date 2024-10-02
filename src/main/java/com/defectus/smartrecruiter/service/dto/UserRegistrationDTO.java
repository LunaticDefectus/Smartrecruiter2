package com.defectus.smartrecruiter.service.dto;

import lombok.Data;

@Data
public class UserRegistrationDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String role;
}