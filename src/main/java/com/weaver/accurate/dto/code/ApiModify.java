package com.weaver.accurate.dto.code;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Package: com.dr.code.diff.dto
 * @Description: java类作用描述
 * @Author: rayduan
 * @CreateDate: 2023/2/27 21:17
 * @Version: 1.0
 * <p>
 */
@Data
//它可以自动生成一个静态内部类（Builder 类）以及相关的链式调用方法，用于构建对象
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiModify {


    /**
     * http api修改
     */
    private Set<HttpApiModify> httpApiModifies;

    /**
     * dubbo api修改
     */
    private Set<ApiMethodModify> dubboApiModifies;




    private Set<ApiMethodModify> customClassModifies;




    private Set<ApiMethodModify> customMethodSignModifies;

}
