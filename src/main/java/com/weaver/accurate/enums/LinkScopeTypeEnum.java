package com.weaver.accurate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum LinkScopeTypeEnum {
    //排除jdk方法
    EXCLUDE_JDK_TYPE(1, "exclude_jdk"),
    //只包本工程groupId的方法
    GROUP_ONLY_TYPE(2, "group_only"),
    //全部调用链
    ALL_TYPE(3, "all"),
    ;

    private Integer code;
    private String value;


    /**
     * 根据code获取值
     *
     * @param code
     * @return
     */
    public static String getValueByCode(Integer code) {
        LinkScopeTypeEnum[] values = LinkScopeTypeEnum.values();
        for (LinkScopeTypeEnum type : values) {
            if (type.code.equals(code)) {
                return type.value;
            }
        }
        return null;
    }

    /**
     * 根据value获取code
     *
     * @param value
     * @return
     */
    public static Integer getCodeByValue(String value) {
        LinkScopeTypeEnum[] values = LinkScopeTypeEnum.values();
        for (LinkScopeTypeEnum type : values) {
            if (type.value.equalsIgnoreCase(value)) {
                return type.code;
            }
        }
        return null;
    }
}
