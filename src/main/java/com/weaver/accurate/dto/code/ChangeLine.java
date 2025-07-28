package com.weaver.accurate.dto.code;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeLine {

    /**
     * 变更类型
     */
    private String type;

    private Integer startLineNum;

    private Integer endLineNum;

}
