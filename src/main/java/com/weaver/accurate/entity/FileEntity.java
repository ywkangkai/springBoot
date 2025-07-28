package com.weaver.accurate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@TableName("tb_file")
public class FileEntity extends BaseEntity{
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String oldFileName;
    private String oldFilePath;
    private String newFileName;
    private String newFilePath;
    @NotBlank(message = "bucketName不能为空")
    private String bucketName;
    private boolean isResult;
    private String result;

    public List<String> getResult() {
        try {
            return result == null ? null : new ObjectMapper().readValue(result, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null; // 或者根据需求返回一个默认值
        }
    }

    public void setResult(List<String> result) {
        try {
            this.result = result == null ? null : new ObjectMapper().writeValueAsString(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            this.result = null; // 或者根据需求设置一个默认值
        }
    }
}
