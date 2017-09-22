package com.zhph.utils;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
/**
 *
 * @ClassName：ZipUtil
 * @Description：文件压缩工具类 （压缩为 .zip文件）
 * @company:zhph
 */
public class ZipUtil {
    /**
     *
     * @Title: compressToZip
     * @param：@param srcfile 要压缩的文件 （可以是多个）
     * @param：@param zipfile 压缩后的文件
     * @return：void
     * @Description：TODO(将文件压缩为.zip文件)
     * @author dailiang
     * @date 2016-7-22 上午9:12:45
     * @throws
     */
    public static void compressToZip(List<File> srcfile, File zipfile) {
        byte[] buf = new byte[1024];
        try {

            // Create the ZIP file
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
            // 设置压缩的时候文件名编码为gbk,不然压缩后文件名为中文的部分会乱码
            out.setEncoding("GBK");

            // Compress the files
            for (int i = 0; i < srcfile.size(); i++) {
                File file = srcfile.get(i);
                if (file.exists()) {
                    FileInputStream in = new FileInputStream(file);
                    // Add ZIP entry to output stream.
                    out.putNextEntry(new ZipEntry(file.getName()));
                    // Transfer bytes from the file to the ZIP file
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    // Complete the entry
                    out.closeEntry();
                    in.close();
                }
            }
            // Complete the ZIP file
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void compressToZJZip(List<String> filePaths, File zipfile) {
        byte[] buf = new byte[1024];
        try {

            // Create the ZIP file
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
            // 设置压缩的时候文件名编码为GBK,不然压缩后文件名为中文的部分会乱码
            out.setEncoding("GBK");

            // Compress the files
            for (int i = 0; i < filePaths.size(); i++) {
                File file = new File(filePaths.get(i));
                if (file.exists()) {
                    FileInputStream in = new FileInputStream(file);
                    // Add ZIP entry to output stream.
                    out.putNextEntry(new ZipEntry(file.getName()));
                    // Transfer bytes from the file to the ZIP file
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    // Complete the entry
                    out.flush();
                    out.closeEntry();
                    in.close();
                    file.delete();
                }
            }
            // Complete the ZIP file
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
