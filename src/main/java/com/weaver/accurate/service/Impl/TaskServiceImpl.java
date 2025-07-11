package com.weaver.accurate.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weaver.accurate.common.ResponseData;
import com.weaver.accurate.dto.input.task.TaskInputDto;
import com.weaver.accurate.entity.ProjectEntity;
import com.weaver.accurate.entity.TaskEntity;
import com.weaver.accurate.mapper.ProjectMapper;
import com.weaver.accurate.mapper.TaskMapper;
import com.weaver.accurate.mapper.TaskModuleMapper;
import com.weaver.accurate.service.TaskService;
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

    //分页查询
    public ResponseData<List<TaskEntity>> queryAll(int pageNum, int pageSize) {
//        方式一
//        LambdaQueryWrapper<TaskEntity> queryWrapper = new LambdaQueryWrapper<>();
//        IPage<TaskEntity> page = new Page<>(pageNum, pageSize);
//        super.page(page, queryWrapper);
//        List<TaskEntity> taskList = page.getRecords();
//        Long total = page.getTotal();
//
//        return ResponseData.success(taskList, total, (int) page.getSize(), (int) page.getCurrent());
//      方式二：
        LambdaQueryWrapper<TaskEntity> queryWrapper = new LambdaQueryWrapper<>();
        IPage<TaskEntity> page = baseMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
        List<TaskEntity> taskList = page.getRecords();
        Long total = page.getTotal();
        return ResponseData.success(taskList, total, (int) page.getSize(), (int) page.getCurrent());
    }


}

