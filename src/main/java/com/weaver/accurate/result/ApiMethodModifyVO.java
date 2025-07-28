package com.weaver.accurate.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiMethodModifyVO {


    /**
     * 方法签名,com/dr/code/diff/controller/CodeDiffController#getSvnList#String, String, String
     */
    @ApiModelProperty(value = "方法签名,com/dr/code/diff/controller/CodeDiffController#getSvnList#String, String, String")
    private String methodSign;

    /**
     * 类名
     */
    @ApiModelProperty(value = "类名")
    private String className;


    /**
     * 方法名称
     */
    @ApiModelProperty(value = "方法名称")
    private String methodName;


    /**
     * 方法参数
     */
    @ApiModelProperty(value = "方法参数")
    private List<String> methodParams;


}
