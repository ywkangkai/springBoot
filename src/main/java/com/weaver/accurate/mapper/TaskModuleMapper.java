package com.weaver.accurate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weaver.accurate.entity.TaskEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TaskModuleMapper extends BaseMapper<TaskEntity> {

    void insertTaskModule(@Param(value = "taskId") Integer taskId, @Param(value = "moduleId") Integer moduleId);
}
