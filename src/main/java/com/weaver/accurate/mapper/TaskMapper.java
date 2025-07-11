package com.weaver.accurate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weaver.accurate.entity.ProjectEntity;
import com.weaver.accurate.entity.TaskEntity;

import java.util.List;

public interface TaskMapper extends BaseMapper<TaskEntity> {
    ProjectEntity selectProjectById(Integer projectId);

    List<TaskEntity> selectTasks(TaskEntity taskEntity);

    TaskEntity selectByNameAndProjectId(TaskEntity taskEntity);

    void updateIsArchive(int taskId);

    List<TaskEntity> task_all();
}
