package com.weaver.accurate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weaver.accurate.entity.ProjectEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectMapper extends BaseMapper<ProjectEntity> {
    // 这里可以添加自定义的查询方法，如果需要的话
    ProjectEntity queryByName(String name);

    int updateProject(ProjectEntity inputDto);

    List<ProjectEntity> ProjectWithModules();

    ProjectEntity queryById(Integer id);
}
