package com.example.travel_project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.travel_project.common.ResponseData;
import com.example.travel_project.dto.input.project.ProjectInputDto;
import com.example.travel_project.entity.ProjectEntity;

import java.util.List;

public interface ProjectService extends IService<ProjectEntity> {

    ResponseData<List<ProjectEntity>> query(
                                            String name,
                                            String leaderName,
                                            String description);

    ResponseData<ProjectInputDto> create(ProjectInputDto inputDto);

    ResponseData<ProjectEntity> queryByName(String name);

    ResponseData<ProjectEntity> update(ProjectEntity inputDto);

    ResponseData Delete(Integer id);
}
