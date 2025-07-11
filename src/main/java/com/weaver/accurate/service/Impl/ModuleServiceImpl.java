package com.weaver.accurate.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weaver.accurate.common.ResponseData;
import com.weaver.accurate.entity.ModuleEntity;
import com.weaver.accurate.mapper.ModuleMapper;
import com.weaver.accurate.service.ModuleService;
import org.springframework.stereotype.Service;

@Service
public class ModuleServiceImpl extends ServiceImpl<ModuleMapper, ModuleEntity> implements ModuleService {

    @Override
    public ResponseData<ModuleEntity> create(ModuleEntity moduleEntity) {
        ResponseData<ModuleEntity> responseData;
        try{
            LambdaQueryWrapper<ModuleEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ModuleEntity::getName, moduleEntity.getName());
            ModuleEntity existingModule = super.getOne(queryWrapper);
            if (existingModule != null) {
                return ResponseData.failure("模块名称已存在");
            }
            super.save(moduleEntity);
            responseData = ResponseData.success(moduleEntity);
        }catch (Exception e) {
            e.printStackTrace();
            responseData = ResponseData.failure("创建模块失败");
        }
        return responseData;
    }
}
