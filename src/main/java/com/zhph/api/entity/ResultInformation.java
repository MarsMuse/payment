package com.zhph.api.entity;



/**
 *
 * @Author: Zou Yao
 * @Description: (响应信息对象)
 * @Time: 2017/7/17 11:07
 *
**/
public class ResultInformation {

    //响应状态码
    private  String code;

    //响应信息
    private  String message;

    //结果编号
    private  String resultNo;

    //扣款渠道
    private String chargeChannelCode;

    public ResultInformation(String code, String message, String resultNo) {
        this.code = code;
        this.message = message;
        this.resultNo = resultNo;
    }

    public ResultInformation(String code, String message) {
        this.code = code;
        this.message = message;
    }


    public ResultInformation() {

    }


    public String getChargeChannelCode() {
        return chargeChannelCode;
    }

    public void setChargeChannelCode(String chargeChannelCode) {
        this.chargeChannelCode = chargeChannelCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResultNo() {
        return resultNo;
    }

    public void setResultNo(String resultNo) {
        this.resultNo = resultNo;
    }

    public void updateInfo(String code , String message , String resultNo){
        this.code = code;
        this.message = message;
        this.resultNo = resultNo;
    }

    public void updateInfo(String code , String message ){
        this.code = code;
        this.message = message;
    }
}
