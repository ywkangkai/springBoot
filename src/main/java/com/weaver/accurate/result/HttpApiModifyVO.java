package com.weaver.accurate.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMethod;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HttpApiModifyVO {


    /**
     * 方法签名,com/dr/code/diff/controller/CodeDiffController#getSvnList#String, String, String
     */
    @ApiModelProperty(value = "方法签名,com/dr/code/diff/controller/CodeDiffController#getSvnList#String, String, String")
    private String methodSign;


    /**
     * url映射,如/api/res/1
     */
    @ApiModelProperty(value = "url映射,如/api/res/1")
    private String mappingUrl;



    /**
     * 映射方法,如get,put
     */
    @ApiModelProperty(value = "映射方法,如get,put")
    private RequestMethod mappingMethod;


}
