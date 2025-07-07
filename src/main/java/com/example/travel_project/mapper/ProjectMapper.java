package com.example.travel_project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.travel_project.common.ResponseData;
import com.example.travel_project.entity.ProjectEntity;

public interface ProjectMapper extends BaseMapper<ProjectEntity> {
    // 这里可以添加自定义的查询方法，如果需要的话
    ProjectEntity queryByName(String name);

    int updateProject(ProjectEntity inputDto);
}
