package com.weaver.accurate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weaver.accurate.common.ResponseData;
import com.weaver.accurate.dto.input.project.ProjectInputDto;
import com.weaver.accurate.entity.ProjectEntity;

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

    ResponseData<List<ProjectEntity>> queryWithModules();
}
