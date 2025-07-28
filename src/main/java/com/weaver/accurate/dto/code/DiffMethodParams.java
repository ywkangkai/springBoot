package com.weaver.accurate.dto.code;

import com.weaver.accurate.enums.CodeManageTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Description: 差异代码参数
 */
@Data
@Builder
public class DiffMethodParams {


    /**
     * git 远程仓库地址
     */
    private String repoUrl;

    /**
     * git原始分支或tag
     */
    private String baseVersion = "";

    /**
     * git现分支或tag
     */
    private String nowVersion = "";


    /**
     * 专用于svn新分支
     */
    private String svnRepoUrl;

    /**
     * 版本控制类型
     */
    private CodeManageTypeEnum codeManageTypeEnum;


    private List<String> excludeFiles;

}
