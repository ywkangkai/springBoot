package com.weaver.accurate.async;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.weaver.accurate.common.LoggerUtil;
import com.weaver.accurate.entity.FileEntity;
import com.weaver.accurate.util.analyzeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AsyncService {
    @Async
    public void ansync_war_jar(String filePath_1, String filePath_2, String server){
        FileEntity fileEntity = new FileEntity();
        QueryWrapper<FileEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_path", filePath_1);
        analyzeUtil analyzeUtil = new analyzeUtil(filePath_1, filePath_2, server);
        List<String> result = analyzeUtil.getResult();
        LoggerUtil.info(log, "异步分析结果：{}", result);
    }
}
