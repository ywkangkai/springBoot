package com.weaver.accurate.util;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceDaoAnalyzer {
    private final Map<String, List<String>> serviceToDaoCalls = new HashMap<>();

    // 解析 Service 方法调用的 Dao 方法
    public void analyzeServiceMethods(List<ClassNode> serviceClasses) {
        for (ClassNode serviceClass : serviceClasses) {
            for (MethodNode method : serviceClass.methods) {
                analyzeMethod(serviceClass, method);
            }
        }
    }

    // 解析方法内部调用
    private void analyzeMethod(ClassNode serviceClass, MethodNode method) {
        List<String> daoCalls = new ArrayList<>();
        if (method.instructions == null) return;

        for (AbstractInsnNode insn : method.instructions) {
            if (insn instanceof MethodInsnNode) {
                MethodInsnNode methodInsn = (MethodInsnNode) insn;

                // 过滤掉标准库方法
                if (methodInsn.owner.startsWith("java/") || methodInsn.owner.startsWith("javax/")) {
                    continue;
                }

                // 识别 Dao 方法（假设 Dao 层类路径中包含 "dao"）
                if (methodInsn.owner.contains("/respository/") || methodInsn.owner.contains("/dao/")) {
                    String service_interface = methodInsn.owner.replace("/", ".") + "." + methodInsn.name + methodInsn.desc;
                    daoCalls.add(service_interface);
                }
            }
        }

        if (!daoCalls.isEmpty()) {
            String methodKey = serviceClass.name.replace("/", ".") + "." + method.name + method.desc;
            serviceToDaoCalls.put(methodKey, daoCalls);
        }
    }

    // 获取 Service -> Dao 调用关系
    public Map<String, List<String>> getServiceToDaoCalls() {
        return serviceToDaoCalls;
    }
}
