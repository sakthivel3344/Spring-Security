package com.example.spring_jwt.dto;

import lombok.Data;

@Data
public class UpdateUserDetailsDto {

    private String userName;
    private String firstName;
    private String lastName;
}
