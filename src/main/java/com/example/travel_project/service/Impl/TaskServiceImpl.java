package com.example.travel_project.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.travel_project.common.ResponseData;
import com.example.travel_project.dto.input.task.TaskInputDto;
import com.example.travel_project.entity.ProjectEntity;
import com.example.travel_project.entity.TaskEntity;
import com.example.travel_project.mapper.ProjectMapper;
import com.example.travel_project.mapper.TaskMapper;
import com.example.travel_project.mapper.TaskModuleMapper;
import com.example.travel_project.service.TaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, TaskEntity> implements TaskService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    TaskModuleMapper taskModuleMapper;
    @Override
    public ResponseData<TaskEntity> createTask(TaskInputDto taskInputDto) {
        ResponseData<TaskEntity> responseData;
        ProjectEntity projectEntity = baseMapper.selectProjectById(taskInputDto.getProjectId());
        if (projectEntity == null) {
            responseData = ResponseData.failure("项目不存在");
            return responseData;
        }
        LambdaQueryWrapper<TaskEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TaskEntity::getName, taskInputDto.getName())
                    .eq(TaskEntity:: getProjectId, taskInputDto.getProjectId())
                    .eq(TaskEntity::getIsDelete, false);
        TaskEntity existingTask = super.getOne(queryWrapper);
        if (existingTask != null) {
            responseData = ResponseData.failure("任务名称已存在");
            return responseData;
        }
        TaskEntity taskEntity = modelMapper.map(taskInputDto, TaskEntity.class);
        super.save(taskEntity);
        responseData = ResponseData.success(taskEntity);
        return responseData;

    }

    @Override
    public ResponseData<List<TaskEntity>> queryTasks(TaskEntity taskInputDto, int pageNum, int pageSize) {
//        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
//            baseMapper.selectTasks(taskInputDto);
//        });
        ResponseData<List<TaskEntity>> responseData;
        PageHelper.startPage(pageNum, pageSize);
        List<TaskEntity> taskList = baseMapper.selectTasks(taskInputDto);
        PageInfo<TaskEntity> page = new PageInfo<>(taskList);
        responseData = ResponseData.success(page.getList(), page.getTotal(), page.getPageSize(), page.getPageNum());
        return responseData;
    }

    @Override
    public ResponseData<TaskEntity> update(TaskEntity taskEntity){
        ResponseData<TaskEntity> responseData;
        ProjectEntity projectEntity = baseMapper.selectProjectById(taskEntity.getProjectId());
        if (projectEntity == null) {
            responseData = ResponseData.failure("项目不存在");
            return responseData;
        }
        try {
           TaskEntity task = baseMapper.selectByNameAndProjectId(taskEntity);
           if (task != null){
               responseData = ResponseData.failure("任务名称已存在");
                return responseData;
           }
           super.updateById(taskEntity);
           responseData = ResponseData.success(taskEntity);
        } catch (Exception e) {
            e.printStackTrace();
            responseData = ResponseData.failure("更新任务失败: " + e.getMessage());
        }
        return responseData;
    }

    @Override
    public ResponseData runTask(int ProjectId, int taskId){
        ResponseData responseData;
        try{
            baseMapper.updateIsArchive(taskId);
            ProjectEntity pro = projectMapper.queryById(ProjectId);
            int module_id = pro.getModules().get(0).getId();
            taskModuleMapper.insertTaskModule(taskId, module_id);
            responseData = ResponseData.success("运行成功");
        }catch (Exception e) {
            e.printStackTrace();
            responseData = ResponseData.failure("运行失败: " + e.getMessage());
        }
        return responseData;
    }


}
