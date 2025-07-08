package com.example.travel_project.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.travel_project.common.ResponseData;
import com.example.travel_project.entity.ModuleEntity;
import com.example.travel_project.mapper.ModuleMapper;
import com.example.travel_project.service.ModuleService;
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
