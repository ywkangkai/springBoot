package com.example.travel_project.controller;

import com.example.travel_project.common.ResponseData;
import com.example.travel_project.service.FileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Tag(name = "文件管理", description = "文件上传、下载、删除等功能")
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    FileService fileService;

    @PostMapping("/upload")
    public ResponseData uploadFile(@RequestBody MultipartFile[] files) {
        return fileService.uploadFile(files);
    }

    @GetMapping("/download/{fileName}")
    public void downloadFile(@PathVariable String fileName, HttpServletResponse response) {
        fileService.downloadFile(fileName, response);
    }
}
