package com.weaver.accurate.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.weaver.accurate.entity.UserEntity;
import com.weaver.accurate.util.SessionUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

//需要在entity下的BaseEntity类中添加相关字段，并使用@TableField注解标记字段的填充方式

//CustomMetaObjectHandler的作用是在MyBatis-Plus执行插入或更新操作时，自动填充创建人、创建时间、更新人、更新时间等字段。

@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {
    @Autowired
    SessionUtil sessionUtil;

    @Override
    public void insertFill(MetaObject metaObject) {
        SessionUtil.CurrentUser currentUser = this.getCurrentUser();
        this.setFieldValByName("createById", currentUser.getUserEntity().getId(), metaObject);
        this.setFieldValByName("createByName", currentUser.getUserEntity().getUsername(), metaObject);
        this.setFieldValByName("updateById", currentUser.getUserEntity().getId(), metaObject);
        this.setFieldValByName("updateByName", currentUser.getUserEntity().getUsername(), metaObject);
        Date current = new Date();
        this.setFieldValByName("createTime", current, metaObject);
        this.setFieldValByName("updateTime", current, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        SessionUtil.CurrentUser currentUser = this.getCurrentUser();
        this.setFieldValByName("updateById", currentUser.getUserEntity().getId(), metaObject);
        this.setFieldValByName("updateByName", currentUser.getUserEntity().getUsername(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    public SessionUtil.CurrentUser getCurrentUser() {
        SessionUtil.CurrentUser currentUser = sessionUtil.getCurrentUser();
        if (currentUser == null) {
            currentUser = new SessionUtil.CurrentUser();
            UserEntity userEntity = new UserEntity();
            userEntity.setId(-1); // 设置一个默认的用户ID，表示未登录状态
            userEntity.setUsername("anonymous"); // 设置一个默认的用户名
            userEntity.setName("匿名用户"); // 设置一个默认的用户名称
            currentUser.setUserEntity(userEntity);
        }
        return currentUser;
    }
}