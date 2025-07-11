package com.weaver.accurate.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weaver.accurate.common.ResponseData;
import com.weaver.accurate.entity.FileEntity;
import com.weaver.accurate.mapper.FileMapper;
import com.weaver.accurate.service.FileService;
import io.minio.*;
import io.minio.errors.*;
import lombok.Data;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Data
public class FileServiceImpl extends ServiceImpl<FileMapper, FileEntity> implements FileService {
    @Value("${file.upload-dir}")
    private String uploadDir;
    private MinioClient minioClient;

    public void get_minioClient(String url, String access, String secret) {
        minioClient = MinioClient.builder().endpoint(url).credentials(access, secret).build();
    }

    public FileServiceImpl(@Value("${minio.url}") String url,
                           @Value("${minio.access}") String access,
                           @Value("${minio.secret}") String secret) {
        // 初始化 MinioClient
        this.get_minioClient(url, access, secret);
    }

    @Override
    public ResponseData<FileEntity> uploadFile(MultipartFile[] files, String bucketName) {
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
//                file.transferTo(targetFile); // 保存文件到本地

                boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
                if (!found) {
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                }
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(file.getInputStream(), -1, 1024 * 1024 * 10) // 不得小于 5 Mib
                        .build());
                FileEntity fileEntity = new FileEntity();
                fileEntity.setFileName(fileName);
                fileEntity.setFilePath(targetFile.getAbsolutePath());
                fileEntity.setFileSize(file.getSize());
                fileEntity.setBucketName(bucketName);
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
    public void downloadFileWithMinio(String fileName, String bucket, HttpServletResponse response, HttpServletResponse request) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 设置响应类型
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        // 获取文件流
        GetObjectResponse res = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(fileName)
                .build());
        // 将文件流输出到响应流
        IOUtils.copy(res, response.getOutputStream());
        // 结束
        response.flushBuffer();
        res.close();
    }
    @Override
    public ResponseData deleteFile(String bucket, String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket) // 替换为实际的桶名
                    .object(fileName) // 假设 fileId 是文件名
                    .build());
            // 删除数据库记录
            FileEntity fileEntity = super.getOne(new QueryWrapper<FileEntity>().eq("file_name", fileName).eq("bucket_name", bucket));
            if (fileEntity != null) {
                super.removeById(fileEntity.getId());
            }
            return ResponseData.success("文件删除成功");
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseData.failure("删除文件失败: " + e.getMessage());
        }
    }
}
