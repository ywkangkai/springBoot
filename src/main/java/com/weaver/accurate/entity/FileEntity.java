package com.weaver.accurate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@TableName("tb_file")
public class FileEntity extends BaseEntity{
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String fileName;
    private String filePath;
    private Long fileSize;
    @NotBlank(message = "bucketName不能为空")
    private String bucketName;
    private boolean isResult;
    private List<String> result;
}
