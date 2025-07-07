package com.example.travel_project.controller;

import com.example.travel_project.common.ResponseData;
import com.example.travel_project.dto.input.task.TaskInputDto;
import com.example.travel_project.entity.TaskEntity;
import com.example.travel_project.service.TaskService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@Tag(name="任务管理", description = "任务管理")
public class TaskController {
    @Autowired
    TaskService taskService;

    @PostMapping("/query")
    public ResponseData<List<TaskEntity>> queryTasks(@RequestBody TaskEntity taskEntity, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return taskService.queryTasks(taskEntity, pageNum, pageSize);
    }

    @PostMapping("/create")
    public ResponseData<TaskEntity> createTask(@RequestBody @Validated TaskInputDto taskInputDto) {
        return taskService.createTask(taskInputDto);
    }

    @PostMapping("/update")
    public ResponseData<TaskEntity> updateTask(@RequestBody @Validated TaskEntity taskEntity) {
        return taskService.update(taskEntity);
    }
}
