package com.example.travel_project.dto.output.user;

import com.example.travel_project.dto.BaseOutputDto;
import lombok.Data;

@Data
public class UserOutputDto extends BaseOutputDto {
    private Integer id;
    private String username;
//    private String password;
    private String name;
    private String email;
    private String mobile;
    private String roles;
    private Boolean isDeleted;
}
