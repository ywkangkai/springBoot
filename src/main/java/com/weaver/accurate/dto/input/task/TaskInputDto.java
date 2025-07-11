package com.weaver.accurate.dto.input.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TaskInputDto {
    @Schema(description = "任务名称")
    @NotBlank(message = "任务名称不能为空")
    private String name;
    @Schema(description = "任务描述")
    private String description;
    @Schema(description = "所属项目")
    @NotNull(message = "所属项目不能为空")
    private Integer projectId;
    private Boolean isDelete = false;
    private Boolean isArchive;
    private Boolean isJob;
    private Integer archiveId;
    private String archiveName;
}
