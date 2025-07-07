package com.example.travel_project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.travel_project.dto.input.task.TaskInputDto;
import com.example.travel_project.entity.ProjectEntity;
import com.example.travel_project.entity.TaskEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

public interface TaskMapper extends BaseMapper<TaskEntity> {
    ProjectEntity selectProjectById(Integer projectId);

    List<TaskEntity> selectTasks(TaskEntity taskEntity);

    TaskEntity selectByNameAndProjectId(TaskEntity taskEntity);
}
