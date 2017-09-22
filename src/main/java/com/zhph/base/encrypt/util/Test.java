package com.zhph.base.encrypt.util;

import com.baofoo.util.PathUtil;
import com.zhph.base.encrypt.aes.AesEncryt;
import com.zhph.base.encrypt.aes.AesKeyUtil;
import com.zhph.base.encrypt.entity.DecryptInformation;
import com.zhph.base.encrypt.entity.InformationSecurityEntity;
import com.zhph.base.encrypt.rsa.RsaEncrypt;
import com.zhph.base.encrypt.rsa.RsaKeyUtil;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Author: zou yao .
 * Created time: 2017/7/17 14.
 * Since: 1.0
 **/
public class Test {


    public static void main(String[] args) {

        /*String  test  =  "aaa测试信息*//*63548123";
        PrivateKey pk = RsaKeyUtil.getPrivateKey("D:\\IdeaSpace\\PaymentPlatform\\src\\main\\resources\\platform\\payment\\payment_pri.pfx", "zhphpayment");
        PublicKey pub = RsaKeyUtil.getPublicKey("D:\\IdeaSpace\\PaymentPlatform\\src\\main\\resources\\platform\\payment\\payment_pub.cer");
        String  encryt = RsaEncrypt.encryptData(test ,pk);
        System.out.println(encryt);
        String de = RsaEncrypt.decryptData(encryt , "D:\\IdeaSpace\\PaymentPlatform\\src\\main\\resources\\platform\\payment\\payment_pub.cer");
        System.out.println(test.equals(de));

        String  sign = SignatureUtil.signature(test , pk);
        System.out.println(sign);
        boolean bo = SignatureUtil.verify(test ,sign,pub );
        System.out.println(bo);
*/
        /*InformationSecurityEntity ise = InformationSecurityUtil.encryptInformation(test ,pub,pk );
        System.out.println(ise);
        DecryptInformation di = InformationSecurityUtil.decryptInformation(ise ,pub, pk );
        System.out.println(di);*/

        /*Key aes = AesKeyUtil.getSecurityKey();
        String enca = AesEncryt.encryptData(test ,aes );
        String ena = AesEncryt.decryptData(enca ,aes);
        System.out.println(test.equals(ena));*/
        System.out.println(MessageDigestUtil.digestToHexString("abc123456"));


    }
}
