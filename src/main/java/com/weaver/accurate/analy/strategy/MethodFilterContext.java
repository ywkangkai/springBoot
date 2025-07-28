package com.weaver.accurate.analy.strategy;


import com.weaver.accurate.enums.LinkScopeTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MethodFilterContext {

    private LinkScopeTypeEnum linKScopeTypeEnum;

    private String className;

    private String baseClassName;

}
