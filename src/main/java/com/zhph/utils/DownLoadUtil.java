package com.zhph.utils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by ZHPH on 2017/8/9.
 */
public class DownLoadUtil {
    public static void downloadFile(HttpServletResponse response,
                                    String fileName, File file) throws IOException {
// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        // 2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)
        response.setHeader("Content-Disposition", "attachment;fileName="
                + java.net.URLEncoder.encode(fileName, "UTF-8"));

        ServletOutputStream out = response.getOutputStream();
        FileInputStream inputStream = new FileInputStream(file);
        int b = 0;
        byte[] buffer = new byte[512];
        while ((b = inputStream.read(buffer)) != -1) {
            // 4.写到输出流(out)中
            out.write(buffer, 0, b);
        }
        inputStream.close();
        out.close();
        out.flush();
    }
}
