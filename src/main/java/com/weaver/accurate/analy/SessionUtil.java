package com.weaver.accurate.analy;

import com.weaver.accurate.entity.UserEntity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.io.Serializable;


//@Component 注解表示该类是一个 Spring 管理的 Bean。Spring 容器会自动检测并注册 SessionUtil 类为一个 Bean，使得我们可以通过 @Autowired 注入它并在其他地方使用

//SessionUtil 类的目的是简化 HTTP 会话中存储和访问当前用户信息的操作。通过它，开发者可以方便地将 CurrentUser 对象保存到会话中、从会话中读取当前用户信息，或清除当前用户信息
@Component
public class SessionUtil {
    @Autowired
    HttpSession httpSession;

    final String CURRENT_USER_KEY = "currentUser";

    public void setCurrentUser(CurrentUser currentUser){
        httpSession.setAttribute(CURRENT_USER_KEY, currentUser);
    }

    public CurrentUser getCurrentUser(){
        CurrentUser currentUser = null;
        Object result = httpSession.getAttribute(CURRENT_USER_KEY);
        if (result != null) {
            currentUser = (CurrentUser) result;
        }
        return currentUser;
    }

    public void removeCurrentUser(){
        httpSession.removeAttribute(CURRENT_USER_KEY);
    }


    @Data
    public static class CurrentUser implements Serializable {
        private UserEntity userEntity;
    }
}
