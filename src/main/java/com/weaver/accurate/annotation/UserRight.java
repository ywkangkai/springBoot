package com.weaver.accurate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
用户权限标注注解，用于标注RestController方法或类
主要作用：
1、认证表示，添加此标识的类或方法需要登录后才能访问
2、授权标识，登录后，也只有具备roles中指定的角色才能访问
注意只有UserRight打在类或者接口上不会直接生效需要配合一个interceptor的拦截器
*/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserRight {
    String[] roles();  //A接口需要admin权限，B接口需要user权限
}
