package com.weaver.accurate.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @date:2021/1/9
 * @className:MethodInfoResultVO
 * @author:Administrator
 * @description: 方法对象
 */
@Data
@ApiModel("方法对象")
public class MethodInfoResultVO {


//    /**
//     * 方法的md5
//     */
//    @ApiModelProperty(name = "md5", value = "方法的md5", dataType = "string", example = "13E2BFB69F7D987A6DB4272400C94E9B")
//    public String md5;
    /**
     * 方法名
     */
    @ApiModelProperty(name = "methodName", value = "方法名", dataType = "string", example = "getAll", position = 1)
    public String methodName;
    /**
     * 方法参数
     */
    @ApiModelProperty(name = "parameters", value = "parameters", dataType = "string", position = 2)
    public List<String> parameters;

    /**
     * 开始行
     */
    @ApiModelProperty(value = "开始行", position = 3)
    private Integer startLine;

    /**
     *
     */
    @ApiModelProperty(value = "结束行", position = 4)
    private Integer endLine;

}
