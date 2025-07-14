package com.weaver.accurate.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weaver.accurate.common.ResponseData;
import com.weaver.accurate.dto.account.LoginInputDto;
import com.weaver.accurate.dto.account.RegisterDto;
import com.weaver.accurate.dto.account.LoginOutputDto;
import com.weaver.accurate.entity.UserEntity;
import com.weaver.accurate.mapper.UserMapper;
import com.weaver.accurate.service.AccountService;
import com.weaver.accurate.service.UserService;
import com.weaver.accurate.util.JWTUtil;
import com.weaver.accurate.util.PasswordUtil;
import com.weaver.accurate.analy.SessionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AccountServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements AccountService {
    @Autowired
    SessionUtil sessionUtil;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserService userService;

    @Override
    public ResponseData<LoginOutputDto> login(LoginInputDto loginInputDto) {
        ResponseData<LoginOutputDto> responseData;
        try{
            QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", loginInputDto.getUsername());
            queryWrapper.eq("is_deleted", false);
            //false 表示如果查询结果为空，不抛出异常
            UserEntity tbUser = userService.getOne(queryWrapper, false);
            if (tbUser != null){
                //equalsIgnoreCase判断两个内容是否相等，同时忽略大小写
                if (tbUser.getPassword().equalsIgnoreCase(PasswordUtil.encrypt(loginInputDto.getPassword(), loginInputDto.getUsername())) == false){
                    responseData = new ResponseData<>();
                    responseData.setCode(400);
                    responseData.setMessage("密码不正确");
                    return responseData;
                }
            }
            else {
                responseData = new ResponseData<>();
                responseData.setCode(400);
                responseData.setMessage("用户名不存在");
                return responseData;
            }
            LoginOutputDto loginOutputDto = modelMapper.map(tbUser, LoginOutputDto.class);

            //JWT登录时返回token
            //验证通过，生成并返回token
            //添加Payload信息们可以是拿到token的用户（如前端，第三方调用方）需要的信息
            HashMap<String,String> tokenInfos = new HashMap<>();
            tokenInfos.put("status", "success");
            //将返回数据同事放到token中，用于认证鱼授权使用的token中的信息进行验证
            tokenInfos.put("data", new ObjectMapper().writeValueAsString(loginOutputDto));
            //生成token
            String token = JWTUtil.generateToken(tokenInfos);
            loginOutputDto.setToken(token);
//            SessionUtil.CurrentUser currentUser = new SessionUtil.CurrentUser();
//            currentUser.setUserEntity(tbUser);
//            sessionUtil.setCurrentUser(currentUser);
            responseData = ResponseData.success(loginOutputDto);
        }catch (Exception exception){
            log.error("登录失败", exception);
            responseData = ResponseData.failure(exception.toString());
        }
        return responseData;
    }

    @Override
    public ResponseData<Boolean> register(RegisterDto registerDto) {
        ResponseData<Boolean> responseData;
        try{
            LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserEntity::getUsername, registerDto.getUsername());
            //false表示如果没查出来不会抛错
            UserEntity existUser = userService.getOne(queryWrapper, false);
            if (existUser == null){
                UserEntity userEntity = modelMapper.map(registerDto, UserEntity.class);
                List<String> roles = new ArrayList<>();
                roles.add("staff");
                userEntity.setRoles(roles);
                Boolean result = userService.save(userEntity);
                responseData = ResponseData.success(result);
            }else {
                responseData = new ResponseData<>();
                responseData.setCode(2);
                responseData.setMessage("用户名已存在");

            }
        }catch (Exception exception){
            log.error("注册失败", exception);
            responseData = ResponseData.failure(exception.toString());
        }
        return responseData;
    }
}
