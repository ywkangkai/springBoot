package com.example.travel_project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.travel_project.common.ResponseData;
import com.example.travel_project.dto.input.task.TaskInputDto;
import com.example.travel_project.entity.TaskEntity;



import java.util.List;

public interface TaskService extends IService<TaskEntity> {

    ResponseData<TaskEntity> createTask(TaskInputDto taskInputDto);

    ResponseData<List<TaskEntity>> queryTasks(TaskEntity taskEntity, int pageNum, int pageSize);

    ResponseData<TaskEntity> update(TaskEntity taskEntity);

    ResponseData runTask(int projectId, int taskId);

    ResponseData<List<TaskEntity>> queryAll(int pageNum, int pageSize);
//    IPage<TaskEntity> queryAll(int pageNum, int pageSize);
}
