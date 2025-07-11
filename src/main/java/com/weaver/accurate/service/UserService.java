package com.weaver.accurate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weaver.accurate.common.ResponseData;
import com.weaver.accurate.dto.input.user.UserCreateInputDto;
import com.weaver.accurate.dto.input.user.UserUpdateputDto;
import com.weaver.accurate.dto.output.user.UserOutputDto;
import com.weaver.accurate.dto.output.user.UserOutputRestPassword;
import com.weaver.accurate.dto.output.user.UserQueryAlloutputDto;
import com.weaver.accurate.entity.UserEntity;

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
