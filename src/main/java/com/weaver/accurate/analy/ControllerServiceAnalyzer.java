package com.weaver.accurate.analy;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.tree.*;

import java.util.*;

public class ControllerServiceAnalyzer {
    private final Map<String, List<String>> controllerToServiceCalls = new HashMap<>();
    private final List<String> controllerMethod = new ArrayList<>();

    private String ControllerName = "";

    // 解析 Controller 方法调用的 Service
    public void analyzeControllerMethods(List<ClassNode> controllers) {
        for (ClassNode controller : controllers) {
            ControllerName = controller.name.replace("/", ".");
            for (MethodNode method : controller.methods) {
                analyzeMethod(controller, method);
            }
        }
    }

    // 解析方法调用的 Service
//    private void analyzeMethod(ClassNode controller, MethodNode method) {
//        String root_value = "";
//        String method_ = "";
//        String methodMappingValue = "";
//        List<String> serviceCalls = new ArrayList<>();
//        List<AnnotationNode> classAnnotations = controller.visibleAnnotations;
//        for (AnnotationNode classAnnotation : classAnnotations) {
//            String desc = classAnnotation.desc;
//            if ((desc.contains("Controller") || desc.contains("Mapping")) && classAnnotation.values != null) {
//                root_value = getAnnotationValue(classAnnotation, "value");
//                break;
//            }
//        }
//        if (method.instructions == null) return;
//        List<AnnotationNode> methodAnnotations = method.visibleAnnotations;
//        if (method.visibleAnnotations != null){
//            for (AnnotationNode methodAnnotation : methodAnnotations){
//                if (!methodAnnotation.desc.contains("Mapping")){
//                    continue;
//                }
//                methodMappingValue = getAnnotationValue(methodAnnotation, "value");
//                if (methodMappingValue.equals("/")) {
//                    methodMappingValue = root_value;
//                }
//                //method是获取方法类型（get，post，delete等）
//                method_ = getMappingAnnotation(methodAnnotation.desc);
//                methodMappingValue = AsmUtil.getAnnotationValue(methodAnnotation, "value");
//                if (methodMappingValue.equals("/")) {
//                    methodMappingValue = root_value;
//                }
//            }
//            if (!methodMappingValue.contains("/")) {
//                methodMappingValue = "/" + methodMappingValue;
//            } else if (methodMappingValue.equals(root_value)){
//                methodMappingValue = root_value;
//            } else if (methodMappingValue.contains("/") && !methodMappingValue.contains(root_value)) {
//                methodMappingValue = root_value + methodMappingValue;
//            }
//        }
//
//        for (AbstractInsnNode insn : method.instructions) {
//            if (insn instanceof MethodInsnNode) {
//                MethodInsnNode methodInsn = (MethodInsnNode) insn;
//                if (methodInsn.name.equals("<init>") || methodInsn.name.equals("<clinit>")) {
//                    continue;
//                }
//                // 过滤掉标准库调用
//                if (methodInsn.owner.startsWith("java/") || methodInsn.owner.startsWith("javax/") || methodInsn.owner.startsWith("org/")) {
//                    continue;
//                }
//
//                // 可能是 Service 调用
//                serviceCalls.add(methodInsn.owner.replace("/", ".") + "." + methodInsn.name + methodInsn.desc);
//            }
//        }
//
//        if (!serviceCalls.isEmpty()) {
////            String methodKey = controller.name.replace("/", ".") + "." + method.name + method.desc;
//            String methodKey = controller.name.replace("/", ".") + "." + method.name + "." + method_ + "." + methodMappingValue;
//            controllerToServiceCalls.put(methodKey, serviceCalls);
//        }
//    }

    private void analyzeMethod(ClassNode controller, MethodNode method) {

        String root_value = "";
        String method_ = "";
        String methodMappingValue = "";
        List<String> serviceCalls = new ArrayList<>();
        List<AnnotationNode> classAnnotations = controller.visibleAnnotations;
        for (AnnotationNode classAnnotation : classAnnotations) {
            String desc = classAnnotation.desc;
            if ((desc.contains("Controller") || desc.contains("Mapping")) && classAnnotation.values != null) {
                root_value = getAnnotationValue(classAnnotation, "value");
                break;
            }
        }
        if (method.instructions == null) return;
        List<AnnotationNode> methodAnnotations = method.visibleAnnotations;
        if (method.visibleAnnotations != null){
            for (AnnotationNode methodAnnotation : methodAnnotations){
                if (!methodAnnotation.desc.contains("Mapping")){
                    continue;
                }
                methodMappingValue = getAnnotationValue(methodAnnotation, "value");
                if (methodMappingValue.equals("/")) {
                    methodMappingValue = root_value;
                }
                //method是获取方法类型（get，post，delete等）
                method_ = getMappingAnnotation(methodAnnotation.desc);
                methodMappingValue = AsmUtil.getAnnotationValue(methodAnnotation, "value");
                if (methodMappingValue.equals("/")) {
                    methodMappingValue = root_value;
                }
            }
            if (!methodMappingValue.contains("/")) {
                methodMappingValue = "/" + methodMappingValue;
            } else if (methodMappingValue.equals(root_value)){
                methodMappingValue = root_value;
            } else if (methodMappingValue.contains("/") && !methodMappingValue.contains(root_value)) {
                methodMappingValue = root_value + methodMappingValue;
            }
        }

        for (AbstractInsnNode insn : method.instructions) {

            if (insn instanceof MethodInsnNode) {
                MethodInsnNode methodInsn = (MethodInsnNode) insn;
                String myname = methodInsn.name;

                if (methodInsn.name.equals("<init>") || methodInsn.name.equals("<clinit>")) {
                    continue;
                }
                // 过滤掉标准库调用
                else if (methodInsn.owner.startsWith("java/") || methodInsn.owner.startsWith("javax/") || methodInsn.owner.startsWith("org/")) {
                    continue;
                }

                else {
                    // 可能是 Service 调用
                    serviceCalls.add(methodInsn.owner.replace("/", ".") + "." + methodInsn.name + methodInsn.desc);
                }
            }
        }

        if (!serviceCalls.isEmpty()) {
            String methodKey = controller.name.replace("/", ".") + "." + method.name + method.desc  + "." + method_ + "." + methodMappingValue;
            controllerToServiceCalls.put(methodKey, serviceCalls);
            //如果controllerMethod中不存在此元素就添加
            if (!controllerMethod.contains(ControllerName + "." + method.name + method.desc)) {
                controllerMethod.add(methodKey);
            }
        }
    }

    // 获取 Controller 调用的 Service
    public Map<String, List<String>> getControllerToServiceCalls() {
        return controllerToServiceCalls;
    }

    public List<String> getControllerMethod() {
        return controllerMethod;
    }

    public static String getAnnotationValue(AnnotationNode classAnnotation, String annotationKey){
        String annotationValue = "";
        List<Object> values = classAnnotation.values;
        if (CollectionUtils.isEmpty(values)) {
            return annotationValue;
        }
        Object mappingValue = values.get(values.indexOf(annotationKey)+1);
        //mappingValue是否是list
        if (mappingValue instanceof List){
            return ((List<Object>) mappingValue).get(0) + "";
        }else {
            return mappingValue + "";
        }
    }

    public static String getMappingAnnotation(String classAnnotation){
        if (StringUtils.isEmpty(classAnnotation)){
            return "";
        }
        String temp = classAnnotation.toUpperCase();
        int i = temp.lastIndexOf("/");
        int i1 = temp.indexOf(";");
        return temp.substring(i+1, i1).replace("MAPPING", "");
    }
}

