package com.weaver.accurate.analy.Bean;

import com.weaver.accurate.enums.MethodNodeTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @Description: 方法信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodInfo {

    /**
     * 类信息
     */
    private ClassInfo classInfo;


    /**
     * 抽象标志
     */
    private Boolean abstractFlag;


    /**
     * 方法名称
     */
    private String methodName;


    /**
     * 方法参数
     */
    private List<String> methodParams;

    /**
     * 参数名称列表
     */
    private List<String> methodParamNameList;
    /**
     * 方法签名,com/dr/code/diff/controller/CodeDiffController#getSvnList#String, String, String
     */
    private String methodSign;


    /**
     * url映射,如/api/res/1
     */
    private String mappingUrl;


    /**
     * 映射方法,如get,put
     */
    private RequestMethod mappingMethod;


    /**
     * 方法节点类型枚举
     */
    private MethodNodeTypeEnum methodNodeTypeEnum;
    /**
     * 调用的方法
     */
    private List<MethodInfo> callerMethods;


    private List<String> visitedMethods;
}
