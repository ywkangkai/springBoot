package com.weaver.accurate.service;



import com.weaver.accurate.analy.Bean.MethodInfo;
import com.weaver.accurate.dto.code.ApiModify;
import com.weaver.accurate.dto.code.DiffInfo;
import com.weaver.accurate.dto.code.DiffMethodParams;
import com.weaver.accurate.dto.code.MethodInvokeParam;

import java.util.List;
import java.util.Map;


public interface CodeDiffService {


    /**
     * @date:2021/1/9
     * @className:CodeDiffService
     * @author:Administrator
     * @description: 获取差异代码
     */
    DiffInfo getDiffCode(DiffMethodParams diffMethodParams);



    /**
     * 得到静态方法调用
     *
     * @param methodInvokeParam 方法调用参数
     * @return {@link ApiModify}
     */
    Map<String,List<MethodInfo>> getStaticMethodInvoke(MethodInvokeParam methodInvokeParam);


}
