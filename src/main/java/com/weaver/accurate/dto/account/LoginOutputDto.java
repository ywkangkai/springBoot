package com.weaver.accurate.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Schema(description = "登出信息")
@Data
public class LoginOutputDto implements Serializable {
    private Integer id;
    private String username;
    private String name;
    private List<String> roles;
    private String token;
}
