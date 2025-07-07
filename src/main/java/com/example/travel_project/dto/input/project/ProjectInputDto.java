package com.example.travel_project.dto.input.project;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ProjectInputDto {
    @NotBlank(message = "项目名称不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5 ]*$", message = "项目名称不能包含特殊符号")
    private String name;
    private Integer LeaderId;
    private String LeaderName;
    private String description;
    private Boolean isDeleted;
}
