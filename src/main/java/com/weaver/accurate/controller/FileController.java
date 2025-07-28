package com.weaver.accurate.controller;

import com.weaver.accurate.common.ResponseData;
import com.weaver.accurate.entity.FileEntity;
import com.weaver.accurate.service.FileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import okhttp3.MediaType;
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

    @PostMapping("/uploadWithMinio")
    public ResponseData uploadWithMinio(MultipartFile[] files, @RequestParam String bucketName) {
        return fileService.uploadFileWithMinio(files, bucketName);
    }

    @GetMapping("/download/{fileName}")
    public void downloadFile(@PathVariable String fileName, HttpServletResponse response) {
        fileService.downloadFile(fileName, response);
    }

    @GetMapping("/downloadWithMinio/{fileName}")
    public void downloadFileWithMinio(@PathVariable String fileName, @RequestParam String bucket, HttpServletResponse response) {
        try {
            fileService.downloadFileWithMinio(fileName, bucket, response, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete")
    public ResponseData deleteFile(@RequestParam String bucket, @RequestParam String fileName) {
        return fileService.deleteFile(bucket, fileName);
    }

    @PostMapping("/analyze/{id}")
    public ResponseData analyze(@PathVariable int id, @RequestParam String old_filePath, @RequestParam String new_filePath, @RequestParam String module) {
        return fileService.analyze(id, old_filePath, new_filePath, module);
    }

    @PostMapping(value = "/uploadFile")
    public ResponseData uploadFile(MultipartFile[] file, @ModelAttribute FileEntity fileEntity) {
        return fileService.uploadFile(file, fileEntity);
    }
}
