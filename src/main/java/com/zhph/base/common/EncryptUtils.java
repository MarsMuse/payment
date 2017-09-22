package com.zhph.base.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.baofoo.rsa.RsaCodingUtil;
import com.baofoo.util.SecurityUtil;

public class EncryptUtils {
    
    private final static String secretKey = "zhenghepuhui";
    
    /**
     * 加密
     * 
     * @param content 需要加密的内容
     * @return
     */
    public static String encrypt(String content) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom random = null;
            try {
                random = SecureRandom.getInstance("SHA1PRNG", "SUN");
                random.setSeed(secretKey.getBytes());
            } catch (NoSuchProviderException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("GBK");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return parseByte2HexStr(result); // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 解密
     * 
     * @param content 待解密内容
     * @return
     */
    public static String decrypt(String content) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(secretKey.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(parseHexStr2Byte(content));
            return new String(result); // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 将二进制转换成16进制
     * 
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
    
    /**
     * 将16进制转换为二进制
     * 
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte)(high * 16 + low);
        }
        return result;
    }

    /**
     * rsa加密数据
     * @param content 要加密的数据
     * @param pfxPath 私钥路径
     * @param pwd 私钥密码
     * @return
     * @throws Exception
     */
    public static String rsaEncrypt(String content,String pfxPath,String pwd) throws Exception{
        String base64str = SecurityUtil.Base64Encode(content);
        // 根据私钥文件对请求参数进行加密
        String encryptMsg = RsaCodingUtil.encryptByPriPfxFile(base64str,
                pfxPath, pwd);
        return encryptMsg;
    }

    /**
     * Base64加密
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String Base64Encode(String str) throws UnsupportedEncodingException {
        return new BASE64Encoder().encode(str.getBytes("UTF-8"));
    }

    /**
     * Base64解密
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static String Base64Decode(String str) throws UnsupportedEncodingException, IOException {
        return new String(new BASE64Decoder().decodeBuffer(str), "UTF-8");
    }

    /**
     * MD5 32位加密
     * @param strContent
     * @return
     */
    public static String MD5_32bits(String strContent) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] tempBytes =  messageDigest.digest(strContent.getBytes("utf-8"));
            StringBuffer stringBuffer = new StringBuffer();
            for(byte bytes: tempBytes) {
                String tempByte = Integer.toHexString(bytes & 0xFF);
                if(tempByte.length() == 1) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(tempByte);
            }
            return stringBuffer.toString();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * MD5 16位加密
     * @param strContent
     * @return
     */
    public static String MD5_16bits(String strContent) {
        return MD5_32bits(strContent).substring(8,24);
    }

    /**
     * 扣款接口返回数据rsa加密
     * @param content
     * @return
     */
//    public static String paymentRsaEncry(String content){
//        String pfxPath = PathUtil.getRootClassPath()+"/"+PaymentUtils.properties.get("base_path")+"/"+PaymentUtils.properties.get("pub_package")+"/"+PaymentUtils.properties.get("pub_pfx_name");
//        // 对数据进行加密
//        try {
//            content = EncryptUtils.rsaEncrypt(content,pfxPath,PaymentUtils.properties.get("pub_pfx_pwd").toString());
//            return content;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
