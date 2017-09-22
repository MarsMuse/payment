package com.zhph.base.utils;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;  
import java.util.Random;
import java.io.OutputStream; 
import java.io.IOException;  
public class ValidateCode  //产生识别验证图像
{   
    public String drawPicture(OutputStream os)
    {
        BufferedImage image = new BufferedImage(85, 35,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        Random r = new Random();
        g.setColor(Color.white);
        g.fillRect(0, 0, 85, 35);
        // 生成一个随机数，并且画到内存映射对象上
//        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String str = "0123456789";
        String number = "";
        for (int i = 0; i < 4; i++) {
            number += str.charAt(r.nextInt(str.length()));
        }
 
        g.setColor(new Color(0, 0, 0));
        g.setFont(new Font("黑体", 18, 22));
        g.drawString(number, 23, 27);
        // 加一些干扰线
//        for (int i = 0; i < 5; i++) {
//            g
//                    .setColor(new Color(r.nextInt(255), r.nextInt(255), r
//                            .nextInt(255)));
//            g.drawLine(r.nextInt(60), r.nextInt(20), r.nextInt(60), r
//                    .nextInt(20));
//        }
        // 2 将图片压缩并输出到客户端
        
        try {
            ImageIO.write(image, "jpeg", os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return number;
    }
}
