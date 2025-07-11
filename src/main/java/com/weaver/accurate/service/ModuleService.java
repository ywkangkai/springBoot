package com.weaver.accurate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.weaver.accurate.common.ResponseData;
import com.weaver.accurate.entity.ModuleEntity;

public interface ModuleService extends IService<ModuleEntity> {
    ResponseData<ModuleEntity> create(ModuleEntity moduleEntity);
}
