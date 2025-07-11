package com.weaver.accurate.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CallChainBuilder {
    private final Map<String, List<String>> controllerToServiceCalls;
    private final Map<String, List<String>> serviceToDaoCalls;

    public CallChainBuilder(Map<String, List<String>> controllerToServiceCalls,
                            Map<String, List<String>> serviceToDaoCalls) {
        this.controllerToServiceCalls = controllerToServiceCalls;
        this.serviceToDaoCalls = serviceToDaoCalls;
    }

    // 递归查找完整调用链
    public List<String> buildCallChains() {
        List<String> callChains = new ArrayList<>();

        for (String controllerMethod : controllerToServiceCalls.keySet()) {
            List<String> serviceMethods = controllerToServiceCalls.get(controllerMethod);
            for (String serviceMethod : serviceMethods) {
//                List<String> chain = new ArrayList<>();
//                chain.add(controllerMethod);
                buildServiceToDaoChain(serviceMethod, callChains, controllerMethod);
            }
        }

        return callChains;
    }

    // 递归解析 Service 到 Dao
    private void buildServiceToDaoChain(String serviceMethod, List<String> callChains, String controllerMethod) {
        String[] result = serviceMethod.split("\\.");
        String method = result[result.length - 1];
        for (String key: serviceToDaoCalls.keySet()){
            if (key.contains(method)){
                String results = controllerMethod + " --> " + serviceMethod + " --> " + key + " --> " + serviceToDaoCalls.get(key).get(0);
                callChains.add(results);
            }
        }



//        List<String> daoMethods = serviceToDaoCalls.get(serviceMethod);
//        if (daoMethods == null || daoMethods.isEmpty()) {
//            // 没有 DAO 调用，终止并存入结果
//            callChains.add(new ArrayList<>(chain));
//            return;
//        }
//
//        // 递归解析 Dao
//        for (String daoMethod : daoMethods) {
//            List<String> newChain = new ArrayList<>(chain);
//            newChain.add(daoMethod);
//            callChains.add(newChain);
//        }
    }
}
