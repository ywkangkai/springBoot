package com.example.travel_project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.travel_project.common.ResponseData;
import com.example.travel_project.entity.ModuleEntity;

public interface ModuleService extends IService<ModuleEntity> {
    ResponseData<ModuleEntity> create(ModuleEntity moduleEntity);
}
