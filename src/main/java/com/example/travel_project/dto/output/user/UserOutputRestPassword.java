package com.example.travel_project.dto.output.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
@Schema(description = "重置密码")
@Data
public class UserOutputRestPassword implements Serializable {
    @NotNull(message = "用户ID不能为空")
    private Integer id;
    @Schema(description = "重置密码")
    @Length(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    @NotNull(message = "密码不能为空")
    private String password;
}
