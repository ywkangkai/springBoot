package com.weaver.accurate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.weaver.accurate.handler.ListTypeHandler;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Data
//autoResultMap = true用法来源于hander/ListTypeHandler
@TableName(value = "tb_user", autoResultMap = true)
public class UserEntity extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String mobile;
    //需要注意，此处修改后需要在对应的dto中修改类型不然会映射失败
    @TableField(jdbcType = JdbcType.VARCHAR, typeHandler = ListTypeHandler.class)
    private List<String> roles;
    private Boolean isDeleted;
}
