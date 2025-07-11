package com.weaver.accurate.dto.output.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserQueryAlloutputDto implements Serializable {
    private Integer id;
    private String username;
    private String name;
}
