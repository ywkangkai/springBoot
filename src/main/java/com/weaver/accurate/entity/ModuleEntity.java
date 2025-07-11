package com.weaver.accurate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@TableName("tb_module")
public class ModuleEntity extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private Integer id;
    @NotBlank(message = "名称不能为空")
    private String name;
    private String description;
    private Boolean isDelete;
    private Integer projectId;
}
