package com.zhph.payment.charge.util;


import com.zhph.payment.charge.entity.ChargeDetailInfo;

/**
 *
 * Author: Zou Yao
 * Description: (扣款数据校验类)
 * Time: 2017/7/24 14:00
 *
**/ 
public class ChargeDataVerify {


    /**
     *
     * Author: zou yao
     * Description: {判断参数是否为空}
     * Date: 2017/7/19 16:59
     *
     **/
    private static boolean  paramIsNull(Object  parameter){

        return parameter== null || "".equals(parameter.toString().trim());
    }


    /**
     *
     * Author: zou yao
     * Description: {批扣数据校验}
     * Date: 2017/7/19 16:33
     *
     **/
    public static boolean  verifyData(ChargeDetailInfo chargeDetailInfo){
        if(chargeDetailInfo == null ){
            return false;
        }
        boolean result = true;
        if(paramIsNull(chargeDetailInfo.getLoanNo())){//合同号
            result = false;
        }else if(paramIsNull(chargeDetailInfo.getLoanName())){//姓名
            result = false;
        }else if(paramIsNull(chargeDetailInfo.getLoanIdCard())){//身份证
            result = false;
        }else if(paramIsNull(chargeDetailInfo.getBankKey())){//银行卡编码
            result = false;
        }else if(paramIsNull(chargeDetailInfo.getAccountNumber())){//银行卡号
            result = false;
        }else if(paramIsNull(chargeDetailInfo.getAmount())){//金额
            result = false;
        }else if(paramIsNull(chargeDetailInfo.getPhoneNumber())){//电话号码
            result = false;
        }else if(paramIsNull(chargeDetailInfo.getMainBody())){//主体
            result = false;
        }
        return result;
    }
}
