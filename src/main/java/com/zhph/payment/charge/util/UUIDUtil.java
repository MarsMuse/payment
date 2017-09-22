package com.zhph.payment.charge.util;


import java.util.Calendar;
import java.util.UUID;

import com.alibaba.druid.util.StringUtils;

/**
 *
 * Author: Zou Yao
 * Description: (获取到UUID的工具类)
 * Time: 2017/7/20 14:02
 *
**/
public class UUIDUtil {


    /**
     *
     * Author: zou yao
     * Description: {获取到32个长度的字符串}
     * Date: 2017/7/21 9:48
     *
    **/
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
    
    /**
     *  取合同交易号
     * @author likang
     * @date 2017-7-25上午11:56:21
     */
    public static  String getChargeNo(String loanNo){
    	try {
			Thread.sleep(1);
			long timeMillis = Calendar.getInstance().getTimeInMillis();
			String transNo = loanNo+String.valueOf(timeMillis).substring(1);
			return transNo;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
