package com.weaver.accurate.util;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.AnnotationNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ControllerMethodScanner {
    private final List<ClassNode> controllerClasses = new ArrayList<>();

    // 解析 JAR/WAR 包或 class 文件目录
    public void scanControllers(String path, String server) {
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalArgumentException("无效的文件路径：" + path);
        }

        if (file.isDirectory()) {
            // 处理 class 目录（开发模式）
            scanClassesFromDirectory(file);
        } else if (file.getName().endsWith(".jar") || file.getName().endsWith(".war")) {
            // 处理 JAR/WAR 包（生产环境）
            scanClassesFromJar(file, server);
        }
    }

    // 处理 class 目录
    private void scanClassesFromDirectory(File classDir) {
        for (File file : Objects.requireNonNull(classDir.listFiles())) {
            if (file.isDirectory()) {
                scanClassesFromDirectory(file); // 递归处理子目录
            } else if (file.getName().endsWith(".class")) {
                try (InputStream fis = new FileInputStream(file)) {
                    processClass(fis);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 处理 JAR/WAR 包
    private void scanClassesFromJar(File jarFile, String server) {
        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class") &&
                        (entry.getName().startsWith("BOOT-INF/classes/") || // Spring Boot JAR
                                entry.getName().startsWith("WEB-INF/classes/com/weaver/" + server + "/controller"))) { // 过滤 META-INF
                    // || !entry.getName().contains("/META-INF/")
                    try (InputStream is = jar.getInputStream(entry)) {
                        processClass(is);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 解析 class 文件，判断是否是 Controller
    private void processClass(InputStream is) throws Exception {
        ClassReader classReader = new ClassReader(is);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        if (isController(classNode)) {
            controllerClasses.add(classNode);
        }
    }

    // 判断是否是 Controller
    private boolean isController(ClassNode classNode) {
        if (classNode.visibleAnnotations == null) return false;
        for (AnnotationNode annotation : classNode.visibleAnnotations) {
            if (annotation.desc.contains("Controller") || annotation.desc.contains("RestController")) {
                return true;
            }
        }
        return false;
    }

//    private boolean isController(ClassNode classNode) {
//        String value = "";
//        if (classNode.visibleAnnotations == null) return false;
//        for (AnnotationNode annotation : classNode.visibleAnnotations) {
//            if (annotation.desc.contains("Controller") || annotation.desc.contains("Mapping") && annotation.values != null) {
//                if (!CollectionUtils.isEmpty(annotation.values)) {
//                    Object mappingValue = annotation.values.get(annotation.values.indexOf("value") + 1);
//                    if (mappingValue instanceof List) {
//                        value = ((List<Object>) mappingValue).get(0) + "";
//                    }else {
//                        value = mappingValue + "";
//                    }
//                }
//                else {
//                    return false;
//                }
//            }
//            System.out.println("ddddd" + value);
//        }
//
//        return true;
//    }

    // 获取所有 Controller 类
    public List<ClassNode> getControllerClasses() {
        return controllerClasses;
    }
}

