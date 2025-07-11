package com.weaver.accurate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tab_category")
public class Category {
    @TableId(value = "id_", type = IdType.AUTO)
    protected Long id;


    private String cname;
}
