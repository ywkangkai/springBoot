package com.weaver.accurate.dto.code;


import com.weaver.accurate.enums.CodeManageTypeEnum;
import lombok.Data;

import java.util.List;

@Data
public class VersionControlDto {

    /**
     *  远程仓库地址
     */
    private String repoUrl;

    /**
     * git原始分支或tag/svn 版本
     */
    private String baseVersion;

    /**
     * git现分支或tag、svn 版本
     */
    private String nowVersion;


    /**
     * 专用于svn新分支
     */
    private String svnRepoUrl;


    /**
     * 本地旧文件基础地址
     */
    private String oldLocalBasePath;

    /**
     * 本地新文件基础地址
     */
    private String newLocalBasePath;

    /**
     * 版本控制类型
     */
    private CodeManageTypeEnum codeManageTypeEnum;



    private List<DiffEntryDto> diffClasses;

}
