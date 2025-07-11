package com.weaver.accurate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

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
    @TableField(exist = false) // 非数据库字段
    private List<ModuleEntity> modules; // 关联的模块实体

}
