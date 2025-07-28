package com.weaver.accurate.dto.code;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Builder
@Data
public class DiffClassInfoResult {
    /**
     * java文件
     */
    private String classFile;
    /**
     * 类名
     */
    private String className;
    /**
     * 包名
     */
    private String packages;


    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 类中的方法
     */
    private List<MethodInfoResult> methodInfos;


    /**
     * 修改类型
     */
    private String type;



    /**
     * 变更行
     */
    private List<ChangeLine> lines;

}
