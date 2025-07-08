package com.example.travel_project.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.travel_project.common.ResponseData;
import com.example.travel_project.dto.account.LoginOutputDto;
import com.example.travel_project.dto.input.project.ProjectInputDto;
import com.example.travel_project.entity.ProjectEntity;
import com.example.travel_project.mapper.ProjectMapper;
import com.example.travel_project.service.ProjectService;
import com.example.travel_project.util.JWTUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, ProjectEntity> implements ProjectService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    HttpServletRequest request;

    @Override
    public ResponseData<List<ProjectEntity>> query(
                                                   String name,
                                                   String leaderName,
                                                   String description){
        ResponseData<List<ProjectEntity>> responseData;
        try {

            List<ProjectEntity> projectList = super.list();
            projectList = (List)projectList.stream().filter(project -> {
                return !project.getIsDeleted();
            }).collect(Collectors.toList());
            responseData = ResponseData.success(projectList);
            responseData.setMessage("查询成功");
        } catch (Exception ex) {
            ex.printStackTrace();
            responseData = ResponseData.failure(ex.getMessage());
        }
        return responseData;
    }

    @Override
    public ResponseData<ProjectInputDto> create(ProjectInputDto inputDto){
        ResponseData<ProjectInputDto> responseData;
        try {
            List<String> errorMsg = new ArrayList<>();
            LambdaQueryWrapper<ProjectEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ProjectEntity::getName, inputDto.getName());
            ProjectEntity existingProject = super.getOne(queryWrapper);
            if (existingProject != null) {
                errorMsg.add("项目名称已存在");
            }
            if (errorMsg.size() > 0){
                String message = errorMsg.stream().collect(Collectors.joining(", "));
                responseData = ResponseData.failure(message);
                return responseData;
            }
            ProjectEntity projectEntity = modelMapper.map(inputDto, ProjectEntity.class);
            super.save(projectEntity);

            ProjectInputDto outputDto = modelMapper.map(projectEntity, ProjectInputDto.class);
            responseData = ResponseData.success(outputDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            responseData = ResponseData.failure(ex.getMessage());
        }
        return responseData;
    }

    @Override
    public ResponseData<ProjectEntity> queryByName(String name) {
        ResponseData<ProjectEntity> responseData;
        ProjectEntity projectEntity = baseMapper.queryByName(name);
        if (projectEntity != null) {
            responseData = ResponseData.success(projectEntity);
        } else {
            responseData = ResponseData.failure("项目不存在");
        }
        return responseData;
    }

    @Override
    public ResponseData<ProjectEntity> update(ProjectEntity inputDto) {
        ResponseData<ProjectEntity> responseData;
        try{
            String token = request.getHeader("Authorization");
            LoginOutputDto loginOutputDto = JWTUtil.getLoginData(token);
            if (!loginOutputDto.getId().equals(inputDto.getLeaderId())) {
                return ResponseData.failure("非项目负责人无法修改");
            }
            LambdaQueryWrapper<ProjectEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ProjectEntity::getName, inputDto.getName());
            ProjectEntity existingProject = super.getOne(queryWrapper);
            if (existingProject != null && !existingProject.getId().equals(inputDto.getId())) {
                return ResponseData.failure("项目名称已存在");
            }
            int num = baseMapper.updateProject(inputDto);
            if (num == 0) {
                return ResponseData.failure("更新失败");
            }
            responseData = ResponseData.success(inputDto);
        }catch (Exception ex) {
            ex.printStackTrace();
            responseData = ResponseData.failure(ex.getMessage());
        }
        return responseData;
    }

    @Override
    public ResponseData Delete(Integer id){
        try{
            String token = request.getHeader("Authorization");
            LoginOutputDto loginOutputDto = JWTUtil.getLoginData(token);
            ProjectEntity projectEntity = super.getById(id);
            if (!loginOutputDto.getId().equals(projectEntity.getLeaderId())){
                return ResponseData.failure("非项目负责人无法删除");
            }
            if (projectEntity == null){
                return ResponseData.failure("项目不存在");
            }
            super.removeById(id);
            return ResponseData.success("项目删除成功");
        }catch (Exception ex) {
            ex.printStackTrace();
            return ResponseData.failure(ex.getMessage());
        }
    }

    @Override
    public ResponseData<List<ProjectEntity>> queryWithModules() {
        ResponseData<List<ProjectEntity>> responseData;
        try {
            List<ProjectEntity> projectList = baseMapper.ProjectWithModules();
            responseData = ResponseData.success(projectList);
        } catch (Exception ex) {
            ex.printStackTrace();
            responseData = ResponseData.failure(ex.getMessage());
        }
        return responseData;
    }

}
