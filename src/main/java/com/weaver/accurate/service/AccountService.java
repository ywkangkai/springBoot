package com.weaver.accurate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weaver.accurate.common.ResponseData;
import com.weaver.accurate.dto.account.LoginInputDto;
import com.weaver.accurate.dto.account.RegisterDto;
import com.weaver.accurate.dto.account.LoginOutputDto;
import com.weaver.accurate.entity.UserEntity;

public interface AccountService extends IService<UserEntity> {
    ResponseData<LoginOutputDto> login(LoginInputDto logindto);

//    ResponseData logout();

    ResponseData<Boolean> register(RegisterDto registerDto);
}
