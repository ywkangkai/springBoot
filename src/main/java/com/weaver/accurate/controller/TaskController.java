package com.weaver.accurate.controller;

import com.weaver.accurate.common.ResponseData;
import com.weaver.accurate.dto.input.task.TaskInputDto;
import com.weaver.accurate.entity.TaskEntity;
import com.weaver.accurate.service.TaskService;
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

    @PostMapping("/run")
    public ResponseData runTask(@RequestParam("projectId") int projectId,
                                            @RequestParam("taskId") int taskId) {
        return taskService.runTask(projectId, taskId);
    }

    @GetMapping("/queryAll")
    public ResponseData<List<TaskEntity>> queryAll(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return taskService.queryAll(pageNum, pageSize);
    }
}
