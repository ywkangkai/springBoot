package com.weaver.accurate.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;


public class DiffAnalyze {
    private final String newProjectPath;
    private final String oldProjectPath;

    private final List<String> diffMethods = new ArrayList<>();
    private final List<String> diffSql = new ArrayList<>();

    public DiffAnalyze(String newProjectPath, String oldProjectPath) {
        this.newProjectPath = newProjectPath;
        this.oldProjectPath = oldProjectPath;
    }

    public List<String> getDiffMethods() {
        return diffMethods;
    }

    public List<String> getDiffSql() {
        return diffSql;
    }

    public void diff() {
        // 获取class文件的差异
        List<String> classDiffs = compareClassFiles(true);
        diffMethods.addAll(classDiffs);

        // 获取XML文件的差异
        List<String> xmlDiffs = compareXmlFiles();
        diffSql.addAll(xmlDiffs);

    }

    private List<String> compareClassFiles(Boolean is_print) {
        List<String> diffMethods = new ArrayList<>();
        //获取到所有的class文件
        List<String> newClasses = loadClassFiles(newProjectPath);
        List<String> oldClasses = loadClassFiles(oldProjectPath);

        // 提取方法的操作码
        Map<String, List<Integer>> newMethodMap = extractMethodOpcodes(newClasses);
        Map<String, List<Integer>> oldMethodMap = extractMethodOpcodes(oldClasses);

        Map<String, List<String>> oldMethodsGrouped = groupMethodsByName(oldMethodMap.keySet());
        Map<String, List<String>> newMethodsGrouped = groupMethodsByName(newMethodMap.keySet());

        Set<String> allMethodNames = new HashSet<>();
        allMethodNames.addAll(oldMethodsGrouped.keySet());
        allMethodNames.addAll(newMethodsGrouped.keySet());

        for (String methodName : allMethodNames) {
            List<String> oldMethods = oldMethodsGrouped.getOrDefault(methodName, Collections.emptyList());
            List<String> newMethods = newMethodsGrouped.getOrDefault(methodName, Collections.emptyList());
            if (oldMethods.isEmpty()) {
                for (String m : newMethods) {
                    if (is_print){
                        diffMethods.add(m);
                    }
                    else {
                        diffMethods.add("新增方法: " + m);
                    }

                }
            } else if (newMethods.isEmpty()) {
                for (String m : oldMethods) {
                    if (is_print){
                        diffMethods.add(m);
                    }
                    else {
                        diffMethods.add("删除方法: " + m);
                    }
                }
            } else {
                // 方法名相同，签名不同 → 修改
                Set<String> oldSet = new HashSet<>(oldMethods);
                Set<String> newSet = new HashSet<>(newMethods);
                for (String m : newSet) {
                    if (!oldSet.contains(m)) {
                        if (is_print){
                            diffMethods.add(m);
                        }
                        else {
                            diffMethods.add("新增方法: " + m);
                        }
                    }
                    else {
                        // 方法名和参数都相同，检查操作码
                        List<Integer> oldOpcodes = oldMethodMap.get(m);
                        List<Integer> newOpcodes = newMethodMap.get(m);
                        if (!oldOpcodes.equals(newOpcodes)) {
                            if (is_print){
                                diffMethods.add(m);
                            }
                            else {
                                diffMethods.add("修改方法: " + m);
                            }
                        }
                    }
                }
            }
        }

        return diffMethods;
    }

    private static Map<String, List<String>> groupMethodsByName(Set<String> methodSignatures) {
        Map<String, List<String>> grouped = new HashMap<>();
        for (String fullSignature : methodSignatures) {
            // 假设方法签名为 "方法名(参数类型...)"，可以用正则或简单提取
            String methodName = fullSignature.contains("(") ? fullSignature.substring(0, fullSignature.indexOf('(')) : fullSignature;
            grouped.computeIfAbsent(methodName, k -> new ArrayList<>()).add(fullSignature);
        }
        return grouped;
    }

    // 比较XML文件中的SQL
    private List<String> compareXmlFiles() {
        List<String> diffSqls = new ArrayList<>();

        //解析出所有的xml文件
        List<File> newXmlFiles = loadXmlFiles(newProjectPath);
        List<File> oldXmlFiles = loadXmlFiles(oldProjectPath);

        // 提取 SQL 的 ID 和 SQL 语句，带上文件路径
        Map<String, String> newSqlMap = extractSqlStatements(newXmlFiles);
        Map<String, String> oldSqlMap = extractSqlStatements(oldXmlFiles);

        // 对比SQL
        for (Map.Entry<String, String> entry : newSqlMap.entrySet()) {
            String sqlId = entry.getKey();
            String newSql = entry.getValue();

            if (!oldSqlMap.containsKey(sqlId)) {
                diffSqls.add("新增SQL: " + sqlId);
            } else {
                String oldSql = oldSqlMap.get(sqlId);
                if (!newSql.equals(oldSql)) {
                    diffSqls.add("修改SQL: " + sqlId);
                }
            }
        }

        // 检查删除的SQL
        for (String sqlId : oldSqlMap.keySet()) {
            if (!newSqlMap.containsKey(sqlId)) {
                diffSqls.add("删除SQL: " + sqlId);
            }
        }

        return diffSqls;
    }

    // 加载XML文件
//    private List<File> loadXmlFiles(String path) {
//        File dir = new File(path);
//        List<File> xmlFiles = new ArrayList<>();
//        if (dir.exists() && dir.isDirectory()) {
//            collectXmlFiles(dir, xmlFiles);
//        }
//        return xmlFiles;
//    }
    private List<File> loadXmlFiles(String path) {
        File dir = new File(path);
        List<File> xmlFiles = new ArrayList<>();

        if (dir.exists()) {
            if (dir.isDirectory()) {
                if (dir.getName().equalsIgnoreCase("mapper")){
                    collectXmlFiles(dir, xmlFiles);
                }
            } else if (path.endsWith(".jar") || path.endsWith(".war")) {
                xmlFiles.addAll(extractXmlFilesFromJarOrWar(dir));
            }
        }

        return xmlFiles;
    }

    private void collectXmlFiles(File dir, List<File> collector) {
        for (File f : Objects.requireNonNull(dir.listFiles())) {
            if (f.isDirectory()) {
                collectXmlFiles(f, collector);
            } else if (f.getName().endsWith(".xml")) {
                collector.add(f);
            }
        }
    }

    // 提取XML文件中的SQL语句
    private Map<String, String> extractSqlStatements(List<File> xmlFiles) {
        Map<String, String> sqlMap = new HashMap<>();
        for (File xmlFile : xmlFiles) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(xmlFile);

                // 获取文件路径作为 SQL ID 的一部分
                String filePath = xmlFile.getAbsolutePath();
                String fileName = filePath.split("\\.xml")[0];
                List<String> filePathParts = Arrays.asList(fileName.split("\\\\"));
                String dir = filePathParts.get(filePathParts.size() - 2);
                String lastPart = filePathParts.get(filePathParts.size() - 1);
                String finalName = dir + "." +lastPart;
                //提取<mapper>标签中的namespace属性的值
                String namespace = document.getDocumentElement().getAttribute("namespace");
                List<String> namespaceParts = Arrays.asList(namespace.split("\\."));
                String Dao = namespaceParts.get(namespaceParts.size() - 1);


                // 提取 <select> 标签
                NodeList selectNodes = document.getElementsByTagName("select");
                for (int i = 0; i < selectNodes.getLength(); i++) {
                    Node node = selectNodes.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String id = element.getAttribute("id");
                        String sql = element.getTextContent().trim();
                        sqlMap.put(finalName + "." + Dao + "_" + id, sql); // 使用文件路径+SQL ID作为唯一键
                    }
                }


                // 提取 <insert> 标签
                NodeList insertNodes = document.getElementsByTagName("insert");
                for (int i = 0; i < insertNodes.getLength(); i++) {
                    Node node = insertNodes.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String id = element.getAttribute("id");
                        String sql = element.getTextContent().trim();
                        sqlMap.put(finalName + "." + Dao + "_" + id, sql); // 使用文件路径+SQL ID作为唯一键
                    }
                }

                // 提取 <update> 标签
                NodeList updateNodes = document.getElementsByTagName("update");
                for (int i = 0; i < updateNodes.getLength(); i++) {
                    Node node = updateNodes.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String id = element.getAttribute("id");
                        String sql = element.getTextContent().trim();
                        sqlMap.put(finalName + "." + Dao + "_" + id, sql); // 使用文件路径+SQL ID作为唯一键
                    }
                }

                // 提取 <delete> 标签
                NodeList deleteNodes = document.getElementsByTagName("delete");
                for (int i = 0; i < deleteNodes.getLength(); i++) {
                    Node node = deleteNodes.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String id = element.getAttribute("id");
                        String sql = element.getTextContent().trim();
                        sqlMap.put(finalName + "." + Dao + "_" + id,sql); // 使用文件路径+SQL ID作为唯一键
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sqlMap;
    }


    private List<String> loadClassFiles(String path) {
        File file = new File(path);
        //Collections.emptyList()表示空列表
        if (!file.exists()) return Collections.emptyList();
        List<String> classFiles = new ArrayList<>();

        if (file.isDirectory()) {
            collectRecursively(file, classFiles);
        } else if (path.endsWith(".jar") || path.endsWith(".war")) {
            classFiles.addAll(extractClassFilesFromJarOrWar(file));
        }
        return classFiles;
    }
    private void collectRecursively(File dir, List<String> collector) {
        /*
        dir.listFiles()：调用 File 对象 dir 的 listFiles() 方法，返回该目录下的所有文件和子目录的数组。如果 dir 不是一个目录，或者发生了 I/O 错误，则返回 null。
        Objects.requireNonNull(...)：这是一个工具方法，用于检查传入的对象是否为 null。如果对象为 null，则抛出一个 NullPointerException 异常
         */
        for (File f : Objects.requireNonNull(dir.listFiles())) {
            if (f.isDirectory()) {
                collectRecursively(f, collector);
            } else if (f.getName().endsWith(".class")) {
                collector.add(f.getAbsolutePath());
            }
        }
    }

    private List<String> extractClassFilesFromJarOrWar(File jarFile) {
        List<String> classPaths = new ArrayList<>();
        try {
            Path tempDir = Files.createTempDirectory("asm_diff_"); //创建临时目录名称为asm_diff_
            tempDir.toFile().deleteOnExit(); //设置临时目录在程序退出时删除

            try (JarInputStream jarInput = new JarInputStream(new FileInputStream(jarFile))) {
                JarEntry entry; //定义一个 JarEntry 对象 entry，表示 .jar 或 .war 文件中的每个条目（文件或目录
                while ((entry = jarInput.getNextJarEntry()) != null) { //遍历 .jar 或 .war 文件中的所有条目，直到没有目录为止
                    if (entry.getName().endsWith(".class")) {
                        File outFile = new File(tempDir.toFile(), entry.getName());//表示将 .class 文件解压到临时目录中的路径
                        outFile.getParentFile().mkdirs(); //确保目标路径的父目录存在（如果不存在则创建）
                        try (OutputStream os = new FileOutputStream(outFile)) { //创建一个 OutputStream 对象 os，用于将 .class 文件写入到临时目录中
                            byte[] buffer = new byte[4096]; //定义一个缓冲区 buffer，大小为 4096 字节
                            int len;
                            //使用 jarInput.read(buffer) 从当前条目中读取数据，并将其写入到 outFile 中，直到读取完成
                            while ((len = jarInput.read(buffer)) != -1) {
                                os.write(buffer, 0, len);
                            }
                        }
                        classPaths.add(outFile.getAbsolutePath());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classPaths;
    }

    private Map<String, List<Integer>> extractMethodOpcodes(List<String> classPaths) {
        Map<String, List<Integer>> result = new HashMap<>();

        for (String cls : classPaths) {
            try (FileInputStream fis = new FileInputStream(cls)) {
                ClassReader cr = new ClassReader(fis);
                ClassNode cn = new ClassNode();
                cr.accept(cn, ClassReader.EXPAND_FRAMES);

                for (MethodNode mn : cn.methods) {
                    List<Integer> opcodes = new ArrayList<>();
                    for (AbstractInsnNode insn : mn.instructions) {
                        if (insn.getOpcode() != -1) {
                            opcodes.add(insn.getOpcode());
                        }
                    }
                    String methodId = cn.name.replace('/', '.') + "." + mn.name + mn.desc;
                    result.put(methodId, opcodes);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private List<File> extractXmlFilesFromJarOrWar(File jarFile) {

        List<File> xmlFiles = new ArrayList<>();
        try {
            Path tempDir = Files.createTempDirectory("xml_temp_");
            tempDir.toFile().deleteOnExit();

            try (JarInputStream jarInput = new JarInputStream(new FileInputStream(jarFile))) {
                JarEntry entry;
                while ((entry = jarInput.getNextJarEntry()) != null) {
                    // 检查当前条目是否是以 .xml 结尾并且位于 mapper 目录下
                    if (entry.getName().endsWith(".xml") && entry.getName().contains("mapper/")) {
                        File outFile = new File(tempDir.toFile(), entry.getName());
                        outFile.getParentFile().mkdirs();  // 确保父目录存在
                        try (OutputStream os = new FileOutputStream(outFile)) {
                            byte[] buffer = new byte[4096];
                            int len;
                            while ((len = jarInput.read(buffer)) != -1) {
                                os.write(buffer, 0, len);
                            }
                        }
                        xmlFiles.add(outFile);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xmlFiles;
    }

    private Map<String, Set<String>> findMethodCallers(List<String> classFiles, String targetClass, String targetMethodName, String targetMethodDesc) {
        Map<String, Set<String>> callers = new HashMap<>();
        for (String classFile : classFiles) {
            try (FileInputStream fis = new FileInputStream(classFile)) {
                ClassReader cr = new ClassReader(fis);
                ClassNode cn = new ClassNode();
                cr.accept(cn, ClassReader.EXPAND_FRAMES);

                for (MethodNode mn : cn.methods) {
                    InsnList instructions = mn.instructions;
                    for (AbstractInsnNode insn : instructions) {
                        if (insn instanceof MethodInsnNode) {
                            MethodInsnNode methodInsn = (MethodInsnNode) insn;
                            if (methodInsn.owner.equals(targetClass)
                                    && methodInsn.name.equals(targetMethodName)
                                    && methodInsn.desc.equals(targetMethodDesc)) {
                                String caller = cn.name.replace('/', '.') + "." + mn.name + mn.desc;
                                callers.computeIfAbsent(targetClass + "." + targetMethodName, k -> new HashSet<>()).add(caller);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return callers;
    }


}




