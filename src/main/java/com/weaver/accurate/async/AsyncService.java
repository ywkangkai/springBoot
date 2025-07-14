package com.weaver.accurate.async;

import com.weaver.accurate.util.analyzeUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AsyncService {
    @Async
    public void ansync_war_jar(String filePath_1, String filePath_2, String server){
        analyzeUtil analyzeUtil = new analyzeUtil(filePath_1, filePath_2, server);
        List<String> result = analyzeUtil.getResult();
        System.out.println("异步分析结果：" + result.size() + "条");
    }
}
