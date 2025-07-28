package com.weaver.accurate.dto.code;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiMethodModify {


    /**
     * 方法签名,com/dr/code/diff/controller/CodeDiffController.getSvnList(String, String, String)
     */
    private String methodSign;

    /**
     * 类名
     */
    private String className;


    /**
     * 方法名称
     */
    private String methodName;


    /**
     * 方法参数
     */
    private List<String> methodParams;


}
