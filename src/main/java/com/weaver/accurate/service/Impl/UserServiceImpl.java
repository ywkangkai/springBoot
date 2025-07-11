package com.weaver.accurate.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weaver.accurate.common.ResponseData;
import com.weaver.accurate.dto.input.user.UserCreateInputDto;
import com.weaver.accurate.dto.input.user.UserUpdateputDto;
import com.weaver.accurate.dto.output.user.UserOutputDto;
import com.weaver.accurate.dto.output.user.UserOutputRestPassword;
import com.weaver.accurate.dto.output.user.UserQueryAlloutputDto;
import com.weaver.accurate.entity.UserEntity;
import com.weaver.accurate.mapper.UserMapper;
import com.weaver.accurate.service.UserService;
import com.weaver.accurate.util.PasswordUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseData<List<UserOutputDto>> query(Long pageIndex, Long pageSize, String username, String name, String email, String mobile) {
        ResponseData<List<UserOutputDto>> responseData;
        try {
            //构建查询条件
            LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
            if (username != null){
                queryWrapper.like(UserEntity::getUsername, username);
            }
            if (name != null){
                queryWrapper.like(UserEntity::getName, name);
            }
            // 分页查询
            IPage<UserEntity> page = new Page<>(pageIndex, pageSize);
            super.page(page, queryWrapper);
            List<UserEntity> userEntities = page.getRecords();
            Long total = page.getTotal();
            //转换DTO
            List<UserOutputDto> userOutputDtos = userEntities.stream().map(user -> modelMapper.map(user, UserOutputDto.class)).collect(Collectors.toList());
            responseData = ResponseData.success(userOutputDtos, total);
        } catch (Exception ex){
            responseData = ResponseData.failure(ex.getMessage());
            ex.printStackTrace();
        }
        return responseData;
    }

    @Override
    public ResponseData<List<UserQueryAlloutputDto>> queryAll() {
        ResponseData<List<UserQueryAlloutputDto>> responseData;
        try {
            //查询所有用户
            List<UserEntity> userEntities = super.list();
            List<UserQueryAlloutputDto> all = userEntities.stream().map(user -> modelMapper.map(user, UserQueryAlloutputDto.class)).collect(Collectors.toList());
            responseData = ResponseData.success(all);
        } catch (Exception ex){
            responseData = ResponseData.failure(ex.getMessage());
            ex.printStackTrace();
        }
        return responseData;
    }

    @Override
    public ResponseData<UserOutputDto> queryById(Integer id) {
        ResponseData<UserOutputDto> responseData;
        try {
            UserEntity userEntity = super.getById(id);
            UserOutputDto userOutputDto = modelMapper.map(userEntity, UserOutputDto.class);
            responseData = ResponseData.success(userOutputDto);
        } catch (Exception ex){
            responseData = ResponseData.failure(ex.getMessage());
            ex.printStackTrace();
        }
        return responseData;
    }

    @Override
    public ResponseData<UserOutputDto> create(UserCreateInputDto inputDto) {
        ResponseData<UserOutputDto> responseData;
        try {
            List<String> errorMsg = new ArrayList<>();

            LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserEntity::getUsername, inputDto.getUsername());
            //检查用户名是否已存在
            UserEntity exisUser = super.getOne(queryWrapper);
            if (exisUser != null) {
                errorMsg.add("用户名已存在。");
            }

            if (errorMsg.size() > 0) {
                String message = errorMsg.stream().collect(Collectors.joining(", "));
                responseData = ResponseData.failure(message);
                return responseData;
            }

            UserEntity userEntity = modelMapper.map(inputDto, UserEntity.class);
            //明文密码转换为密文
            String realPassword = PasswordUtil.encrypt(userEntity.getPassword(), userEntity.getUsername());
            userEntity.setPassword(realPassword);
            super.save(userEntity);
            //转换为输出DTO
            UserOutputDto userOutputDto = modelMapper.map(userEntity, UserOutputDto.class);
            responseData = ResponseData.success(userOutputDto);
        } catch (Exception ex){
            responseData = ResponseData.failure(ex.getMessage());
            ex.printStackTrace();
        }
        return responseData;
    }

    @Override
    public ResponseData<UserOutputDto> update(UserUpdateputDto inputDto) {
        ResponseData<UserOutputDto> responseData;
        try {
            UserEntity userEntity = modelMapper.map(inputDto, UserEntity.class);
            super.updateById(userEntity);
            UserOutputDto userOutputDto = modelMapper.map(userEntity, UserOutputDto.class);
            responseData = ResponseData.success(userOutputDto);
        } catch (Exception ex){
            responseData = ResponseData.failure(ex.getMessage());
            ex.printStackTrace();
        }
        return responseData;
    }

    @Override
    public ResponseData delete(Integer id) {
        ResponseData responseData;
        try {
            //逻辑删除用户
            UserEntity userEntity = super.getById(id);
            userEntity.setIsDeleted(true);
            super.updateById(userEntity);
            responseData = ResponseData.success();
        } catch (Exception ex){
            responseData = ResponseData.failure(ex.getMessage());
            ex.printStackTrace();
        }
        return responseData;
    }

    @Override
    public ResponseData reset(UserOutputRestPassword inputDto){
        ResponseData responseData;
        try{
            List<String> errorMsg = new ArrayList<>();
            //检查用户是否存在
            UserEntity userEntity = super.getById(inputDto.getId());
            if (userEntity == null) {
                errorMsg.add("用户不存在。");
            }
            if (errorMsg.size() > 0){
                String message = errorMsg.stream().collect(Collectors.joining(", "));
                responseData = new ResponseData();
                responseData.setCode(400);
                responseData.setMessage(message);
                return responseData;
            }
            userEntity.setPassword(PasswordUtil.encrypt(inputDto.getPassword(), userEntity.getUsername()));
            super.updateById(userEntity);
            responseData = ResponseData.success("密码重置成功。");
        } catch (Exception ex) {
            responseData = ResponseData.failure(ex.getMessage());
            ex.printStackTrace();
        }
        return responseData;
    }

    @Override
    public ResponseData<List<UserOutputDto>> queryAllNotDeleted(){
        ResponseData<List<UserOutputDto>> responseData;
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getIsDeleted, false);
        List<UserEntity> userEntities = super.list(queryWrapper);
        List<UserOutputDto> userOutputDtos = userEntities.stream().
                map(user -> modelMapper.map(user, UserOutputDto.class)).collect(Collectors.toList());
        responseData = new ResponseData<>();
        responseData.setCode(200);
        responseData.setData(userOutputDtos);
        return responseData;
    }
}
