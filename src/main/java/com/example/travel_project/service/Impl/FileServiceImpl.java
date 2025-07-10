package com.example.travel_project.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.travel_project.common.ResponseData;
import com.example.travel_project.entity.FileEntity;
import com.example.travel_project.mapper.FileMapper;
import com.example.travel_project.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FileEntity> implements FileService {
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Override
    public ResponseData<FileEntity> uploadFile(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return ResponseData.failure("上传的文件不能为空");
        }
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            if (!".jar".equalsIgnoreCase(suffix) && ! ".war".equalsIgnoreCase(suffix) && !".jpg".equalsIgnoreCase(suffix)) {
                return ResponseData.failure("只允许上传 .jar 或 .war 文件");
            }
//            String NenwFileName = UUID.randomUUID() + suffix;
            File targetDir = new File(uploadDir);
            if (!targetDir.exists()) {
                targetDir.mkdirs(); // 创建目录
            }
            try {
                File targetFile = new File(targetDir.getAbsoluteFile() + File.separator + fileName);
                file.transferTo(targetFile); // 保存文件
                FileEntity fileEntity = new FileEntity();
                fileEntity.setFileName(fileName);
                fileEntity.setFilePath(targetFile.getAbsolutePath());
                fileEntity.setFileSize(file.getSize());
                super.save(fileEntity);
            }catch (Exception e) {
                e.printStackTrace();
                return ResponseData.failure("文件上传失败: " + e.getMessage());
            }

        }
        return ResponseData.success("文件上传成功");
    }

    @Override
    public void downloadFile(String fileName, HttpServletResponse response){

        try {
            InputStream inputStream = new FileInputStream(uploadDir + File.separator + fileName);
            response.reset();
            response.setContentType("application/octet-stream");
            String filename = new File(uploadDir + File.separator + fileName).getName();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            inputStream.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResponseData<Void> deleteFile(Integer fileId) {
        // 实现文件删除逻辑
        // ...
        return new ResponseData<>();
    }
}
