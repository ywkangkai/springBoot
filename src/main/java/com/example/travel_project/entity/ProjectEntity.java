package com.example.travel_project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_project")
public class ProjectEntity extends BaseEntity{
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String name;
    private Integer LeaderId;
    private String LeaderName;
    private String description;
    private Boolean isDeleted;
}
