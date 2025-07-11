package com.weaver.accurate.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class BaseEntity implements Serializable {
    //创建相关信息
    @TableField(fill= FieldFill.INSERT)
    private Integer createById;
    @TableField(fill= FieldFill.INSERT)
    private String createByName;
    @TableField(fill= FieldFill.INSERT)
    private Date createTime;

    //更新相关信息
    @TableField(fill= FieldFill.INSERT_UPDATE)
    private Integer updateById;
    @TableField(fill= FieldFill.INSERT_UPDATE)
    private String updateByName;
    @TableField(fill= FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
