package com.weaver.accurate.dto.code;

import com.weaver.accurate.enums.CodeManageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MethodInvokeDto {

    /**
     *  远程仓库地址
     */
    private String repoUrl;

    /**
     * 分支或tag
     */
    private String branchName = "";



    /**
     * 版本控制类型
     */
    private CodeManageTypeEnum codeManageTypeEnum;

}
