package com.example.travel_project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.travel_project.entity.ProjectEntity;
import com.example.travel_project.entity.TaskEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskModuleMapper extends BaseMapper<TaskEntity> {

    void insertTaskModule(@Param(value = "taskId") Integer taskId, @Param(value = "moduleId") Integer moduleId);
}
