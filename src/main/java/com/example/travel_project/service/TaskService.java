package com.example.travel_project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.travel_project.common.ResponseData;
import com.example.travel_project.dto.input.task.TaskInputDto;
import com.example.travel_project.entity.TaskEntity;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface TaskService extends IService<TaskEntity> {

    ResponseData<TaskEntity> createTask(TaskInputDto taskInputDto);

    ResponseData<List<TaskEntity>> queryTasks(TaskEntity taskEntity, int pageNum, int pageSize);

    ResponseData<TaskEntity> update(TaskEntity taskEntity);
}
