package com.small.rose.demo.base.utils;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @Project: demo-boot3-m
 * @Author: 张小菜
 * @Description: [ SmallFileUtils ] 说明： 无
 * @Function: 功能描述： 无
 * @Date: 2025/11/30 周日 20:05
 * @Version: v1.0
 */
public class SmallFileUtils {

    public static List<String> readFile(String fileName) {
        if(!FileUtil.exist(fileName)) {
            try{
                FileUtil.newFile(fileName).createNewFile();
            }catch(IOException e){
                throw new RuntimeException(e);
            }
        }
        FileReader reader = new FileReader(fileName);
        return reader.readLines();
    }
    public static <T> T readFile(String fileName, FileReader.ReaderHandler<T> readerHandler) {
        if(!FileUtil.isFile(fileName) || !FileUtil.exist(fileName)) {
            throw new IllegalArgumentException("没有文件" + fileName);
        }
        FileReader reader = new FileReader(fileName);
        return reader.read(readerHandler);
    }
    /**
     *
     * @param fileName  完整的文件路径
     * @param textList  要写的文件内容
     */
    public static void writeFileAppend(String fileName, List<String> textList) {
        FileWriter writer = new FileWriter(fileName);
        writer.writeLines(textList, true);
    }

    /**
     *
     * @param fileName  完整的文件路径
     * @param content
     */
    public static void writeFileAppend(String fileName, String content) {
        FileWriter writer = new FileWriter(fileName);
        writer.writeLines(Arrays.asList(content), true);
    }
}
