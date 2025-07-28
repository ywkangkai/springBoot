package com.weaver.accurate.dto.code;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiffInfo {
    private String oldProjectPath; // 老项目路径
    private String newProjectPath; // 新项目路径
    private List<DiffClassInfoResult> diffClasses;
}
