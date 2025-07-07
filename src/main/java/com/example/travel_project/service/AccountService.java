package com.example.travel_project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.travel_project.common.ResponseData;
import com.example.travel_project.dto.account.LoginInputDto;
import com.example.travel_project.dto.account.RegisterDto;
import com.example.travel_project.dto.account.LoginOutputDto;
import com.example.travel_project.entity.UserEntity;

public interface AccountService extends IService<UserEntity> {
    ResponseData<LoginOutputDto> login(LoginInputDto logindto);

//    ResponseData logout();

    ResponseData<Boolean> register(RegisterDto registerDto);
}
