package com.weaver.accurate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum CodeManageTypeEnum {
    //待通知
    GIT(0,"git"),

    SVN(1,"svn"),;

    private Integer code;
    private String value;
    

    /**
     * 根据code获取值
     * @param code
     * @return
     */
    public static String getValueByCode(Integer code) {
        CodeManageTypeEnum[] values = CodeManageTypeEnum.values();
        for (CodeManageTypeEnum type : values) {
            if (type.code.equals(code)) {
                return type.value;
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
        CodeManageTypeEnum[] values = CodeManageTypeEnum.values();
        for (CodeManageTypeEnum type : values) {
            if (type.value.equalsIgnoreCase(value)) {
                return type.code;
            }
        }
        return null;
    }
}
