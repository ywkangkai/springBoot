package com.example.travel_project.controller;

import com.example.travel_project.annotation.UserRight;
import com.example.travel_project.common.ResponseData;
import com.example.travel_project.dto.input.project.ProjectInputDto;
import com.example.travel_project.entity.ProjectEntity;
import com.example.travel_project.service.ProjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="项目管理", description = "项目管理")
@RestController
@RequestMapping("/project")
//@UserRight(roles = {"admin", "staff"})
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @GetMapping("/query")
    public ResponseData<List<ProjectEntity>> query(
                                                   @RequestParam(required = false) String name,
                                                   @RequestParam(required = false) String leaderName,
                                                   @RequestParam(required = false) String description) {
        return projectService.query(name, leaderName, description);
    }

    @PostMapping("/create")
    public ResponseData<ProjectInputDto> create(@RequestBody @Validated ProjectInputDto inputDto) {
        return projectService.create(inputDto);
    }

    @PostMapping("/queryByName")
    public ResponseData<ProjectEntity> queryByName(@RequestParam String name) {
        return projectService.queryByName(name);
    }

    @PostMapping("/update")
    public ResponseData<ProjectEntity> update(@RequestBody @Validated ProjectEntity inputDto) {
        return projectService.update(inputDto);
    }

    @PostMapping("/delete")
    public ResponseData<Void> Delete(@RequestParam Integer id) {
        return projectService.Delete(id);
    }

    @GetMapping("/queryWithModules")
    public ResponseData<List<ProjectEntity>> queryWithModules() {
        return projectService.queryWithModules();
    }
}
