package com.zhph.payment.charge.service.impl;


import com.alibaba.fastjson.JSON;
import com.zhph.api.entity.ChargeOperationException;
import com.zhph.api.entity.PlatformCodeAndData;
import com.zhph.api.entity.ResultInformation;
import com.zhph.api.entity.TransInformation;
import com.zhph.base.common.PropertyMap;
import com.zhph.base.encrypt.entity.DecryptInformation;
import com.zhph.base.encrypt.entity.InformationSecurityEntity;
import com.zhph.base.encrypt.util.InformationSecurityUtil;
import com.zhph.base.listener.WebContextListener;
import com.zhph.payment.charge.dao.WorkPlatformInfoDao;
import com.zhph.payment.charge.entity.WorkPlatformEntity;
import com.zhph.payment.charge.service.PlatformSecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 *
 * Author: Zou Yao
 * Description: (平台安全服务类)
 * Time: 2017/7/17 13:14
 *
**/
@Service
public class PlatformSecurityServiceImpl implements PlatformSecurityService {

    //注入支付平台密钥信息
    @Resource(name="paymentKeyInfo")
    private PropertyMap paymentKeyInfo;

    //注入获取到业务平台信息的数据访问对象
    @Resource
    private WorkPlatformInfoDao workPlatformInfoDao;
    //日志打印对象
    private Logger log = LoggerFactory.getLogger(PlatformSecurityServiceImpl.class);

    /**
     *
     * Author: zou yao
     * Description: {对扣款请求进行初步验证，解密，与完整性校验}
     * Date: 2017/7/17 13:33
     * Param: tis
     *
    **/
    @Override
    public ResultInformation verifyRequestInfo(String  info) throws Exception{
        log.info("平台安全服务--对密文进行初步验证，解密，与完整性校验");
        ResultInformation  resultInfo;
        try {
            TransInformation tis = JSON.parseObject(info, TransInformation.class);
            //如果无法转化成信息传输对象，则参数有误
            if (tis == null || tis.getPlatformCode() == null || "".equals(tis.getPlatformCode())) {
                log.error("平台安全服务--请求数据无法转化成信息传输对象");
                throw new ChargeOperationException("2", "请求参数异常");
            }
            //在不存在密文或者的情况下直接将基本信息封装至接口
            if(tis.getCipherData() == null || "".equals(tis.getCipherData()) ){
                log.error("平台安全服务--解析密文出现异常");
                throw new ChargeOperationException("2", "解析密文出现异常");
            }
            //获取到业务平台信息
            List<WorkPlatformEntity> workPlatformList = workPlatformInfoDao.getPlatformInforByCode(tis.getPlatformCode());
            //如果不存在业务平台信息或者存在信息不止一条则表示基本信息异常
            if (workPlatformList == null || workPlatformList.size() != 1) {
                log.error("平台安全服务--业务平台基本信息异常");
                throw new ChargeOperationException("2", "业务平台基本信息异常");
            }
            //获取到根路径名称
            String pfxPath = paymentKeyInfo.getValue("rootPath");
            if (pfxPath.charAt(pfxPath.length() - 1) != '/') {
                pfxPath = pfxPath + "/";
            }
            //私钥文件路径
            String decryptKeyPfxPath = WebContextListener.getClassRoot() + pfxPath + paymentKeyInfo.getValue("privateKeyName");
            //私钥密码
            String decryptKeyPassword = paymentKeyInfo.getValue("privateKeyPassword");
            //获取到唯一的一条有效平台信息
            WorkPlatformEntity wpe = workPlatformList.get(0);
            //获取到公钥文件路径
            String cerPath = wpe.getCertificatePath();
            //判断结尾是否有斜线
            if (cerPath.charAt(cerPath.length() - 1) != '/') {
                cerPath = cerPath + "/";
            }
            String signaturePublicKeyPath = WebContextListener.getClassRoot() + cerPath + wpe.getCertificateName();
            //新建安全信息对象
            InformationSecurityEntity ise = new InformationSecurityEntity(tis.getEncryptKey(), tis.getCipherData(), tis.getPlatformSignature());
            //信息安全工具解密数据
            DecryptInformation di = InformationSecurityUtil.decryptInformation(ise, signaturePublicKeyPath, decryptKeyPfxPath, decryptKeyPassword);
            if ("1".equals(di.getCode())) {
                //获取到平台号，扣款渠道编号，扣款明文信息
                PlatformCodeAndData pcd = new PlatformCodeAndData(tis.getPlatformCode() , tis.getChannelNo(),di.getData() ,tis.getMainBody());
                resultInfo = new ResultInformation(di.getCode(),JSON.toJSONString(pcd));
            } else if("2".equals(di.getCode())){
                log.error("平台安全服务--解析密文出现异常 ： {}" ,di.getData());
                throw new  ChargeOperationException(di.getCode(), di.getData());
            } else{
                log.error("平台安全服务--解析密文出现异常");
                throw new ChargeOperationException("2", "解析密文出现异常");
            }
        }catch (Exception e){
            log.error("平台安全服务--请求数据出现异常",e);
            if( e instanceof  ChargeOperationException){
                throw e;
            }else{
                e.printStackTrace();
                throw new ChargeOperationException("2", "请求参数异常");
            }
        }
        return resultInfo;
    }



    /**
     *
     * Author: zou yao
     * Description: {加密信息,包含平台信息}
     * Date: 2017/7/21 11:55
     *
    **/
    @Override
    public String encryptPushInfo(String content , String  cerPath , String cerName) throws Exception{
        String result;
        log.error("平台安全服务--加密信息,包含平台信息");
        try {
            //获取到根路径名称
            String pfxPath = paymentKeyInfo.getValue("rootPath");
            String pfxName = paymentKeyInfo.getValue("privateKeyName");
            //获取到支付平台代码信息
            String paymentPlatformCode = paymentKeyInfo.getValue("platformCode");
            if (paymentPlatformCode == null || "".equals(paymentPlatformCode.trim())) {
                log.error("平台安全服务--支付平台Code读取出现异常");
                throw new ChargeOperationException("支付平台Code读取出现异常");
            }
            //验证证书信息
            if (cerPath == null || cerName == null || pfxPath == null || pfxName == null) {
                log.error("平台安全服务--加密密钥异常");
                throw new ChargeOperationException("加密密钥异常");
            } else if ("".equals(cerPath.trim()) || "".equals(cerName.trim()) ||
                    "".equals(pfxPath.trim()) || "".equals(pfxName.trim())) {
                log.error("平台安全服务--加密密钥异常");
                throw new ChargeOperationException("加密密钥异常");
            }

            if (pfxPath.charAt(pfxPath.length() - 1) != '/') {
                pfxPath = pfxPath + "/";
            }
            //私钥文件路径
            String signatureKeyPfxPath = WebContextListener.getClassRoot() + pfxPath + pfxName;
            //私钥密码
            String signatureKeyPassword = paymentKeyInfo.getValue("privateKeyPassword");
            //判断结尾是否有斜线
            if (cerPath.charAt(cerPath.length() - 1) != '/') {
                cerPath = cerPath + "/";
            }
            //加密公钥证书地址
            String encryptPublicKeyPath = WebContextListener.getClassRoot() + cerPath + cerName;
            log.info("平台安全服务--业务平台公钥地址 {} " , encryptPublicKeyPath);
            InformationSecurityEntity ise = InformationSecurityUtil.encryptInformation(content, encryptPublicKeyPath, signatureKeyPfxPath, signatureKeyPassword);

            //传输信息实体
            TransInformation tis = new TransInformation(paymentPlatformCode, ise.getEncryptKey(), ise.getCipherData(), ise.getPlatformSignature());
            result = JSON.toJSONString(tis);
        }catch (Exception e){
            log.error("平台安全服务--加密数据异常" ,e);
            if( e instanceof  ChargeOperationException){
                throw e;
            }else{
                throw new ChargeOperationException( "加密数据异常");
            }
        }
        return result;
    }
}
