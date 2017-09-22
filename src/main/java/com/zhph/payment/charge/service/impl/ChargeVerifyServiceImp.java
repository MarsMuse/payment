package com.zhph.payment.charge.service.impl;


import com.alibaba.fastjson.JSON;
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
import com.zhph.payment.charge.service.ChargeVerifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 *
 * @Author: Zou Yao
 * @Description: (扣款验证服务实现类)
 * @Time: 2017/7/17 13:14
 *
**/
@Service
public class ChargeVerifyServiceImp implements ChargeVerifyService {

    //注入支付平台密钥信息
    @Resource(name="paymentKeyInfo")
    private PropertyMap paymentKeyInfo;

    //注入获取到业务平台信息的数据访问对象
    @Resource
    private WorkPlatformInfoDao workPlatformInfoDao;

    //日志打印对象
    private Logger log = LoggerFactory.getLogger(ChargeVerifyServiceImp.class);

    /**
     *
     * @Author: zou yao
     * @Description: {对扣款请求进行初步验证，解密，与完整性校验}
     * @Date: 2017/7/17 13:33
     * @Param: tis
     *
    **/
    @Override
    public ResultInformation verifyRequestInfo(String  info) {
        log.info("扣款验证--对扣款请求进行初步验证，解密，与完整性校验");
        ResultInformation  resultInfo = null;
        try {
            TransInformation tis = JSON.parseObject(info, TransInformation.class);
            //如果无法转化成信息传输对象，则参数有误
            if (tis == null || tis.getPlatformCode() == null || "".equals(tis.getPlatformCode())) {
                resultInfo = new ResultInformation("2", "请求参数异常");
                log.error("扣款验证--求参数异常--无法转化成信息传输对象");
                return resultInfo;
            }
            //获取到业务平台信息
            List<WorkPlatformEntity> workPlatformList = workPlatformInfoDao.getPlatformInforByCode(tis.getPlatformCode());
            //如果不存在业务平台信息或者存在信息不止一条则表示基本信息异常
            if (workPlatformList == null || workPlatformList.size() != 1) {
                resultInfo = new ResultInformation("2", "业务平台基本信息异常");
                log.error("扣款验证--业务平台基本信息异常--不存在业务平台信息或者存在信息不止一条");
                return resultInfo;
            }
            //私钥文件路径
            String decryptKeyPfxPath = WebContextListener.getClassRoot() + paymentKeyInfo.getValue("rootPath") + paymentKeyInfo.getValue("privateKeyName");
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
                log.info("扣款验证--解析密文成功");
            } else if("2".equals(di.getCode())){
                resultInfo = new ResultInformation(di.getCode(), di.getData());
            } else{
                resultInfo = new ResultInformation("2", "解析密文出现异常");
                log.error("扣款验证--解析密文出现异常");
            }
        }catch (Exception e){
            resultInfo = new ResultInformation("2", "解析密文出现异常");
            log.error("扣款验证--解析密文出现异常",e);
            e.printStackTrace();
        }
        return resultInfo;
    }
}
