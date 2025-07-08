package com.example.travel_project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_task_module")
public class TaskModuleEntity extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer taskId;
    private Integer moduleId;
}
