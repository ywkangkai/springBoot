package com.weaver.accurate.util;

import com.weaver.accurate.analy.*;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class analyzeUtil {
    private List<String> result = new ArrayList<>();


    public analyzeUtil(String NewFile, String OldFile, String server) {
        DiffAnalyze diffAnalyze = new DiffAnalyze(NewFile, OldFile);
        diffAnalyze.diff();
        List<String> diffMethods = diffAnalyze.getDiffMethods();
        ControllerMethodScanner controllerMethodScanner = new ControllerMethodScanner();
        //传递模块的服务
        controllerMethodScanner.scanControllers(NewFile, server);

        List<ClassNode> controllers = controllerMethodScanner.getControllerClasses();
        ControllerServiceAnalyzer controllerServiceAnalyzer = new ControllerServiceAnalyzer();
        controllerServiceAnalyzer.analyzeControllerMethods(controllers);
        Map<String, List<String>> controllerToServiceCalls = controllerServiceAnalyzer.getControllerToServiceCalls();
        List<String> controllerMethod = controllerServiceAnalyzer.getControllerMethod();
        ServiceMethodScanner serviceScanner = new ServiceMethodScanner();
        serviceScanner.scanServices(NewFile);
        List<ClassNode> services = serviceScanner.getServiceClasses();

        ServiceDaoAnalyzer serviceDaoAnalyzer = new ServiceDaoAnalyzer();
        serviceDaoAnalyzer.analyzeServiceMethods(services);
        Map<String, List<String>> serviceToDaoCalls = serviceDaoAnalyzer.getServiceToDaoCalls();

        CallChainBuilder callChainBuilder = new CallChainBuilder(controllerToServiceCalls, serviceToDaoCalls);
        List<String> callChains = callChainBuilder.buildCallChains();

        List<String> mergedList = Stream.concat(controllerMethod.stream(), callChains.stream())
                .collect(Collectors.toList());
        for (String method : mergedList) {
            for (String diffMethod : diffMethods) {
                if (diffMethod.contains("getCompanyApproveForm")){
                    String a = "";
                }
                if (method.contains(diffMethod)) {
                    result.add(diffMethod + ":" + method);
                }
            }
        }
    }

    public List<String> getResult() {
        return result;
    }
}
