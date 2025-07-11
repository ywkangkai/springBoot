package com.weaver.accurate.controller;

import com.weaver.accurate.common.ResponseData;
import com.weaver.accurate.dto.account.LoginInputDto;
import com.weaver.accurate.dto.account.LoginOutputDto;
import com.weaver.accurate.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="账户管理", description = "账户管理")
@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping("/login")
    public ResponseData<LoginOutputDto> login(@RequestBody @Validated LoginInputDto loginInputDto){
        return accountService.login(loginInputDto);
    }
}
