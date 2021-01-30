package com.java.yxt.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.text.SimpleDateFormat;
@Slf4j
public class FileUtil {


    /**
     * 将文件写入到指定目录
     * @param content
     *
     */
    public static void write(String dirName,String content) {
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss'.csv'").format(System.currentTimeMillis());
        writeContentToLocalFile(dirName,fileName,content,"UTF-8");
    }


    /**
     *
     * @param dirName  目录名
     * @param localfilename 文件名
     * @param content 文件内容
     * @param charset 编码
     */
    public static void writeContentToLocalFile(String dirName,String localfilename,String content,String charset) {
        String path = dirName;
        File f=new File(dirName);
        if (!f.exists())
        {
            f.mkdirs();
        }
        String localFilename = dirName+File.separator+localfilename;
        File file = new File(localFilename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                log.error("新建文件异常！",e);
            }
        }
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(file), charset)));
            out.println(content);
        } catch (IOException e) {
            log.error("输出文件异常！",e);
        } finally {
            out.flush();
        }
    }

    /**
     * 删除文件夹及所有子文件
     */
    static void deleteDirectory(String dir){
        File dirFile = new File(dir+"/");
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.isDirectory()) return;
        File[] listFiles = dirFile.listFiles();
        try {
            FileUtils.deleteQuietly(listFiles[0]);
            FileUtils.deleteDirectory(dirFile);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}