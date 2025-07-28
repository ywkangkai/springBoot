package com.weaver.accurate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum MethodNodeTypeEnum {
    //普通方法
    //http方法
    HTTP(1, "http"),
    //dubbo方法
    DUBBO(2, "dubbo"),

    //自定义
    CUSTOM_CLASS(3, "custom_class"),
    CUSTOM_METHOD(4, "custom_method"),
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
        MethodNodeTypeEnum[] values = MethodNodeTypeEnum.values();
        for (MethodNodeTypeEnum type : values) {
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
        MethodNodeTypeEnum[] values = MethodNodeTypeEnum.values();
        for (MethodNodeTypeEnum type : values) {
            if (type.value.equalsIgnoreCase(value)) {
                return type.code;
            }
        }
        return null;
    }
}
