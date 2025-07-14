package com.weaver.accurate.interceptor;//package com.example.auto_java.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.weaver.accurate.annotation.UserRight;
import com.weaver.accurate.common.ResponseData;
import com.weaver.accurate.dto.account.LoginOutputDto;
import com.weaver.accurate.util.JWTUtil;
import com.weaver.accurate.analy.SessionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Component
public class Authenticationlnterceptor implements HandlerInterceptor {
    @Autowired
    SessionUtil sessionUtil;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        //handler：当前拦截的信息都在这里
        //如果不是RestController注解的Action方法，则不进行拦截
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //检查是否有接口权限过滤注解
        //获取当前拦截的handlerMethod方法的@UserRight注解，如果没有，则获取当前拦截的handlerMethod方法所在类的@UserRight注解
        UserRight userRight = handlerMethod.getMethodAnnotation(UserRight.class);
        if (userRight == null){
            //获取当前拦截的handlerMethod方法所在类的@UserRight注解
            Class clazz = handlerMethod.getBean().getClass();
            userRight = (UserRight) clazz.getAnnotation(UserRight.class);
        }
        if (userRight == null){
            //没有@UserRight注解，直接放行
            return true;
        }

        //认证
//        SessionUtil.CurrentUser currentUser = sessionUtil.getCurrentUser();
        String token = request.getHeader("Authorization");
        if (token != null){
            //请求中存在token，验证token
            //去掉Bearer前缀
            token = token.replace("Bearer ", "");
            String errorMessage;
            try{
                //验证token
                JWTUtil.verify(token);
                //如果token验证成功，错误信息未null
                errorMessage = null;
            }catch (SignatureVerificationException ex){
                errorMessage = "无效签名";
                ex.printStackTrace();
            }catch (TokenExpiredException ex){
                errorMessage = "token已过期";
                ex.printStackTrace();
            }catch (AlgorithmMismatchException ex){
                errorMessage = "算法不匹配";
                ex.printStackTrace();
            }catch (Exception ex){
                errorMessage = "token验证失败" + ex.getMessage();
                ex.printStackTrace();
            }
            if (errorMessage != null){
                ResponseData responseData = new ResponseData();
                responseData.setCode(401);
                responseData.setMessage(errorMessage);
                String json = new ObjectMapper().writeValueAsString(responseData);
                response.setStatus(401);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().println(json);
                return false;
            }
            //是否有权限
            //将当前接口需要的角色转换成list
            List<String> needRoles = Arrays.asList(userRight.roles());
            //获取当前用户的角色列表，使用通过验证的token中的载荷信息获取
            List<String> hasRoles = null;
            LoginOutputDto loginOutputDto = JWTUtil.getLoginData(token);
            if (loginOutputDto != null){
                hasRoles = loginOutputDto.getRoles();
            }
            //当前用户角色为空或者当前用户角色没有包括当前接口的角色，则不通过
            if (hasRoles ==null || hasRoles.stream().filter(item -> needRoles.contains(item)).count() <= 0){
                //没有权限
                ResponseData responseData = new ResponseData();
                responseData.setCode(403);
                responseData.setMessage("拒绝访问：未被授权");
                String json = new ObjectMapper().writeValueAsString(responseData);
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().println(json);
                return false;
            }

        }

//        if (currentUser != null){
//            //已登录进行授权验证
//            //是否有权限的处理
//            //将当前接口需要的角色转换成list
//            List<String> needRoles = Arrays.asList(userRight.roles());
//            //获取当前用户的角色列表
//            List<String> hasRoles = currentUser.getUserEntity().getRoles();
//            //当前用户角色为空或者当前用户角色没有包括当前接口的角色，则不通过
//            if (hasRoles ==null || hasRoles.stream().filter(item->needRoles.contains(item)).count() <= 0){
//                //没有权限
//                ResponseData responseData = new ResponseData();
//                responseData.setCode(403);
//                responseData.setMessage("没有权限");
//                String json = new ObjectMapper().writeValueAsString(responseData);
//                response.setStatus(403);
//                response.setContentType("application/json;charset=UTF-8");
//                response.getWriter().println(json);
//                return false;
//            }
//        }
        else {
            //没有登录
            ResponseData responseData = new ResponseData();
            responseData.setCode(401);
            responseData.setMessage("请先登录");
            String json = new ObjectMapper().writeValueAsString(responseData);
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(json);
            return false;
        }
        return true;
    }
}

