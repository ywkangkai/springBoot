package com.weaver.accurate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weaver.accurate.common.ResponseData;
import com.weaver.accurate.dto.input.task.TaskInputDto;
import com.weaver.accurate.entity.TaskEntity;


import java.util.List;

public interface TaskService extends IService<TaskEntity> {

    ResponseData<TaskEntity> createTask(TaskInputDto taskInputDto);

    ResponseData<List<TaskEntity>> queryTasks(TaskEntity taskEntity, int pageNum, int pageSize);

    ResponseData<TaskEntity> update(TaskEntity taskEntity);

    ResponseData runTask(int projectId, int taskId);

    ResponseData<List<TaskEntity>> queryAll(int pageNum, int pageSize);
//    IPage<TaskEntity> queryAll(int pageNum, int pageSize);
}
