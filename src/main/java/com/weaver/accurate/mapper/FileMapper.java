package com.weaver.accurate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weaver.accurate.entity.FileEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper extends BaseMapper<FileEntity> {
    // 这里可以添加自定义的查询方法
    // 例如：List<FileEntity> findFilesByProjectId(Integer projectId);
}
