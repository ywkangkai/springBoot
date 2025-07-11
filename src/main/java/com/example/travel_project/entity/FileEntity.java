package com.example.travel_project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.lang.reflect.Type;

@Data
@TableName("tb_file")
public class FileEntity extends BaseEntity{
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String bucketName;
}
