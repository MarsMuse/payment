package com.zhph.payment.charge.entity;



/**
 *
 * Author: zou yao
 * Description: {用于获取银行信息编码缓存的实体}
 * Date: 2017/8/7 14:36
 *
**/
public class BankCodeCacheEntity {

    private String bankCodeKey;

    private String bankCodeValue;

    public String getBankCodeKey() {
        return bankCodeKey;
    }

    public void setBankCodeKey(String bankCodeKey) {
        this.bankCodeKey = bankCodeKey;
    }

    public String getBankCodeValue() {
        return bankCodeValue;
    }

    public void setBankCodeValue(String bankCodeValue) {
        this.bankCodeValue = bankCodeValue;
    }
}
