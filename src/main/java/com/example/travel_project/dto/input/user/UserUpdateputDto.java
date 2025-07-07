package com.example.travel_project.dto.input.user;

import lombok.Data;

import java.util.List;

@Data
public class UserUpdateputDto {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String mobile;
    private List<String> roles;
}
