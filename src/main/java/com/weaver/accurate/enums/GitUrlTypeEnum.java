package com.weaver.accurate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum GitUrlTypeEnum {
    //http
    HTTP(1,"http"),

    SSH(2,"ssh"),;

    private Integer code;
    private String value;
    

    /**
     * 根据code获取值
     * @param code
     * @return
     */
    public static String getValueByCode(Integer code) {
        GitUrlTypeEnum[] values = GitUrlTypeEnum.values();
        for (GitUrlTypeEnum type : values) {
            if (type.code.equals(code)) {
                return type.value;
            }
        }
        return null;
    }

    /**
     * 根据code获取值
     * @param code
     * @returngetTypeEnumByCode
     */
    public static GitUrlTypeEnum getTypeEnumByCode(Integer code) {
        GitUrlTypeEnum[] values = GitUrlTypeEnum.values();
        for (GitUrlTypeEnum type : values) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 根据value获取code
     * @param value
     * @return
     */
    public static Integer getCodeByValue(String value) {
        GitUrlTypeEnum[] values = GitUrlTypeEnum.values();
        for (GitUrlTypeEnum type : values) {
            if (type.value.equalsIgnoreCase(value)) {
                return type.code;
            }
        }
        return null;
    }
}
