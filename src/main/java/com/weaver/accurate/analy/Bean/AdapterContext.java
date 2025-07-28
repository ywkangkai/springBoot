package com.weaver.accurate.analy.Bean;


import com.weaver.accurate.analy.strategy.MethodFilterContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdapterContext {

    /**
     * 调用链过滤上下文
     */
    private MethodFilterContext methodFilterContext;

    /**
     * dubbo xml指定的类
     */
    private List<String> dubboClasses;
}
