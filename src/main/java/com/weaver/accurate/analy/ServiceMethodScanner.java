package com.weaver.accurate.analy;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ServiceMethodScanner {
    private final List<ClassNode> serviceClasses = new ArrayList<>();

    // 扫描 Service 类
    public void scanServices(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalArgumentException("无效的文件路径：" + path);
        }

        if (file.isDirectory()) {
            scanClassesFromDirectory(file);
        } else if (file.getName().endsWith(".jar") || file.getName().endsWith(".war")) {
            scanClassesFromJar(file);
        }
    }

    // 递归遍历目录中的 .class 文件
    private void scanClassesFromDirectory(File classDir) {
        for (File file : Objects.requireNonNull(classDir.listFiles())) {
            if (file.isDirectory()) {
                scanClassesFromDirectory(file);
            } else if (file.getName().endsWith(".class")) {
                try (InputStream fis = new FileInputStream(file)) {
                    processClass(fis);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 扫描 JAR/WAR 包
    private void scanClassesFromJar(File jarFile) {
        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class") &&
                        (entry.getName().startsWith("BOOT-INF/classes/") ||
                                entry.getName().startsWith("WEB-INF/classes/") ||
                                !entry.getName().contains("/META-INF/"))) {

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

    // 解析 class 文件并判断是否是 Service 类
    private void processClass(InputStream is) throws Exception {
        ClassReader classReader = new ClassReader(is);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        if (isService(classNode)) {
            serviceClasses.add(classNode);
        }
    }

    // 判断类是否是 Service（基于 @Service 注解）
    private boolean isService(ClassNode classNode) {
        if (classNode.visibleAnnotations == null) return false;
        for (AnnotationNode annotation : classNode.visibleAnnotations) {
            if (annotation.desc.contains("Service")) {
                return true;
            }
        }
        return false;
    }

    // 获取所有 Service 类
    public List<ClassNode> getServiceClasses() {
        return serviceClasses;
    }
}
