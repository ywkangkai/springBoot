package com.example.travel_project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.travel_project.common.ResponseData;
import com.example.travel_project.dto.input.user.UserCreateInputDto;
import com.example.travel_project.dto.input.user.UserUpdateputDto;
import com.example.travel_project.dto.output.user.UserOutputDto;
import com.example.travel_project.dto.output.user.UserOutputRestPassword;
import com.example.travel_project.dto.output.user.UserQueryAlloutputDto;
import com.example.travel_project.entity.UserEntity;

import java.util.List;

public interface UserService extends IService<UserEntity> {
    //分页查询
    ResponseData<List<UserOutputDto>> query(Long pageIndex,
                                             Long pageSize,
                                             String username,
                                             String name,
                                             String email,
                                             String mobile);
    //查询所有
    ResponseData<List<UserQueryAlloutputDto>> queryAll();
    //根据ID查询
    ResponseData<UserOutputDto> queryById(Integer id);
    //创建
    ResponseData<UserOutputDto> create(UserCreateInputDto inputDto);
    //修改
    ResponseData<UserOutputDto> update(UserUpdateputDto inputDto);
    //删除
    ResponseData delete(Integer id);

    //重置密码
    ResponseData reset(UserOutputRestPassword inputDto);

    //查询非逻辑删除的用户
    ResponseData<List<UserOutputDto>> queryAllNotDeleted();
}
