package com.weaver.accurate.util;


public class PathUtils {

    /**
     * @description: 获取类本地地址
     */
    public static String getClassFilePath(String baseDir, String classPath) {
        StringBuilder builder = new StringBuilder(baseDir);
        builder.append("/");
        builder.append(classPath);
        return builder.toString();
    }


}
