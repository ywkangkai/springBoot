package com.example.travel_project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.travel_project.common.ResponseData;
import com.example.travel_project.entity.FileEntity;

import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface FileService extends IService<FileEntity> {

    /**
     * 上传文件
     * @param file 文件
     * @return ResponseData<FileEntity>
     */
    ResponseData<FileEntity> uploadFile(MultipartFile[] file, String bucketName);

    /**
     * 下载文件
     * @param fileName 文件名
     * @return ResponseData<byte[]>
     */
    void downloadFile(String fileName, HttpServletResponse response);

    /**
     * 删除文件
     * @param  文件名
     * @return ResponseData<Void>
     */
    ResponseData deleteFile(String bucket, String fileName);

    void downloadFileWithMinio(String fileName, String bucket, HttpServletResponse response, HttpServletResponse request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;


}
