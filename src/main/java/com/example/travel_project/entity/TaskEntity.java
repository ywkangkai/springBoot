package com.example.travel_project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "tb_task")
public class TaskEntity extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private Boolean isDelete;
    private Boolean isArchive;
    private Boolean isJob;
    private Integer archiveId;
    private String archiveName;
    private Integer projectId;
}
