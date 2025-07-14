package com.weaver.accurate.analy;

import com.weaver.accurate.util.MethodInstruction;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AsmUtil {
    public static String methodAllName(ClassNode classNode, MethodNode methodNode) {
        return classNode.name + "." + methodNode.name + "[" + methodNode.desc.substring(1,
                methodNode.desc.lastIndexOf(")")) + "]";
    }

    public static void buildprojectClassNode(File file, List<ClassNode> classNodes, Map<String, List<String>> interfaceMap) {
        if ((!file.exists())) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                buildprojectClassNode(f, classNodes, interfaceMap);
            }
        }else if (file.getName().endsWith(".class")) {
            try (FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath())) {
                ClassReader cr = new ClassReader(fileInputStream);
                //解析文件并返回类的元信息（类名，方法，字段，接口）
                ClassNode cn = new ClassNode();
                cr.accept(cn, 0);
                classNodes.add(cn);
                //获取接口对应的实现类
                List<String> interfaces = cn.interfaces;
                if (!interfaces.isEmpty()) {

                    for (String aninterface : interfaces) {
                        //如果map中已经存在该接口就直接覆盖他
                        if (interfaceMap.containsKey(aninterface)) {
                            List<String> ls = interfaceMap.get(aninterface);
                            ls.add(cn.name);
                            interfaceMap.put(aninterface, ls);
                        }else {
                            List<String> ls = new ArrayList<>();
                            ls.add(cn.name);
                            interfaceMap.put(aninterface, ls);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    //处理函数节点内的所有函数调用指令
    public static List<MethodInstruction> methodInvokeIstructions(MethodNode methodNode) {
        List<MethodInstruction> list = new ArrayList<>();
        InsnList insnList = methodNode.instructions;
        for (int i = 0; i < insnList.size(); i++) {
            AbstractInsnNode insnNode = insnList.get(i);
            int opcode = insnNode.getOpcode();
            /*
                * INVOKEVIRTUAL: 调用实例方法 182
                * INVOKESTATIC: 调用静态方法 184
                * INVOKESPECIAL: 调用构造方法，私有方法，父类方法 183
                * INVOKEINTERFACE: 调用接口方法 185
                * INVOKEDYNAMIC: 动态方法调用 186
            */
            if (opcode == Opcodes.INVOKEVIRTUAL ||
                    opcode == Opcodes.INVOKESTATIC ||
                    opcode == Opcodes.INVOKESPECIAL ||
                    opcode == Opcodes.INVOKEINTERFACE ||
                    opcode == Opcodes.INVOKEDYNAMIC) {
                //DiffMethodVisitor 是一个自定义的访问者，用于访问字节码指令
                DiffMethodVisitor visitor = new DiffMethodVisitor(Opcodes.ASM9);
                //通过 accept(visitor) 方法，将当前指令 insnNode 交给 visitor 进行处理
                insnNode.accept(visitor);
                MethodInstruction instruction = visitor.getInstruction();
                if (instruction != null && instruction.getOwner() != null) {
                    list.add(instruction);
                }
            }
        }
        return list.stream().distinct().collect(Collectors.toList());
    }

    public static List<String> getInterfacfe(List<List<String>> links){
        if (CollectionUtils.isEmpty(links)){
            return new ArrayList<>();
        }
        return links.stream().map(item -> item.get(0)).distinct().collect(Collectors.toList());
    }

    public static boolean classContainsMethod(ClassNode classNode, String methodName, String methodDesc) {
        if (classNode == null || classNode.methods == null) {
            return false;
        }
        for (MethodNode method : classNode.methods) {
            if (method.name.equals(methodName) && method.desc.equals(methodDesc)) {
                return true;
            }
        }
        return false;
    }

    public static MethodNode classMatchingMethod(ClassNode classNode, String methodName, String methodDesc) {
        if (classNode == null || classNode.methods == null) {
            return null;
        }
        for (MethodNode method : classNode.methods) {
            if (method.name.equals(methodName) && method.desc.equals(methodDesc)) {
                return method;
            }
        }
        return null;
    }




}

