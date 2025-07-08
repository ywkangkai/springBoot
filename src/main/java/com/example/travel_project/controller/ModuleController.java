package com.example.travel_project.controller;

import com.example.travel_project.common.ResponseData;
import com.example.travel_project.entity.ModuleEntity;
import com.example.travel_project.service.ModuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/module")
@RestController
@Tag(name = "模块管理", description = "模块管理")
public class ModuleController {
    @Autowired
    ModuleService moduleService;

    @PostMapping("/create")
    public ResponseData<ModuleEntity> create(@RequestBody @Validated ModuleEntity moduleEntity) {
        return moduleService.create(moduleEntity);
    }
}
