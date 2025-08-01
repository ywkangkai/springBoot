package com.weaver.accurate.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Schema(description = "登录信息")
@Data
public class LoginInputDto {
    @Schema(description = "用户名")
    @NotNull(message = "用户名不能为空")
    @Length(min = 2, max = 20, message = "用户名长度必须在2-20个字符之间")
    private String username;

    @Schema(description = "密码")
    @NotNull(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;


}
