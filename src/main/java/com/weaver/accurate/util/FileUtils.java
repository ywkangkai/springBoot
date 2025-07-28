package com.weaver.accurate.util;

import java.io.File;


public class FileUtils {
    /**
     * 搜索文件是否存在对应后缀的文件
     *
     * @param directory 目录
     * @param suffix    后缀
     * @return boolean
     */
    public static boolean searchFile(File directory,String suffix) {
        if (directory == null || !directory.isDirectory()) {
            return false;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return false;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                if (searchFile(file,suffix)) {
                    return true;
                }
            } else if (file.getName().endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
}
