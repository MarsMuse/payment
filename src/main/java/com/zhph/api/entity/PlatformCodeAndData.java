package com.zhph.api.entity;



/**
 *
 * @Author: Zou Yao
 * @Description: (平台代码与明文数据)
 * @Time: 2017/7/18 18:08
 *
**/
public class PlatformCodeAndData {

    //平台编码
    private String platformCode;
    //主体
    private String mainBody;
    //渠道编号
    private String channelNo;
    //数据(明文)
    private String data;

    public PlatformCodeAndData(String platformCode, String channelNo, String data, String mainBody) {
        this.platformCode = platformCode;
        this.mainBody = mainBody;
        this.channelNo = channelNo;
        this.data = data;
    }

    public PlatformCodeAndData(String platformCode, String channelNo, String data) {
        this.platformCode = platformCode;
        this.channelNo = channelNo;
        this.data = data;
    }

    public PlatformCodeAndData() {
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public String getMainBody() {
        return mainBody;
    }

    public void setMainBody(String mainBody) {
        this.mainBody = mainBody;
    }
}
