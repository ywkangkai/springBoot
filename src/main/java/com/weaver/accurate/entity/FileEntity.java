package com.weaver.accurate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
