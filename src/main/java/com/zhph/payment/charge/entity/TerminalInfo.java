package com.zhph.payment.charge.entity;

import java.util.Date;

/**
 * 终端信息
 * Created by zhph on 2017/1/18.
 */
public class TerminalInfo {
    private String priNumber; //唯一标识
    private String terminalName; //终端名称
    private String cerPackage; //公钥保存路径
    private String cerName; //公钥文件名称
    private Date createTime;

    public String getCerName() {
        return cerName;
    }

    public void setCerName(String cerName) {
        this.cerName = cerName;
    }

    public String getPriNumber() {
        return priNumber;
    }

    public void setPriNumber(String priNumber) {
        this.priNumber = priNumber;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getCerPackage() {
        return cerPackage;
    }

    public void setCerPackage(String cerPackage) {
        this.cerPackage = cerPackage;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
