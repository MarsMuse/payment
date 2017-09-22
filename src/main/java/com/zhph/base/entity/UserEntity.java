package com.zhph.base.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UserEntity implements Serializable {
    
    private String id;

    
    private String officeId;
    

    
    private String password;

    
    private String no;

    
    private String name;

    
    private String email;

    
    private String phone;

    
    private String photo;

    
    private BigDecimal status;

    
    private String remarks;

    
    private String createId;

    
    private Date createTime;

    
    private String updateId;

    
    private Date updateTime;

    
    private String idCard;

    
    public String getId() {
        return id;
    }

    
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    
    public String getOfficeId() {
        return officeId;
    }

    
    public void setOfficeId(String officeId) {
        this.officeId = officeId == null ? null : officeId.trim();
    }


    
    public String getPassword() {
        return password;
    }

    
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    
    public String getNo() {
        return no;
    }

    
    public void setNo(String no) {
        this.no = no == null ? null : no.trim();
    }

    
    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    
    public String getEmail() {
        return email;
    }

    
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    
    public String getPhone() {
        return phone;
    }

    
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    
    public String getPhoto() {
        return photo;
    }

    
    public void setPhoto(String photo) {
        this.photo = photo == null ? null : photo.trim();
    }

    
    public BigDecimal getStatus() {
        return status;
    }

    
    public void setStatus(BigDecimal status) {
        this.status = status;
    }

    
    public String getRemarks() {
        return remarks;
    }

    
    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    
    public String getCreateId() {
        return createId;
    }

    
    public void setCreateId(String createId) {
        this.createId = createId == null ? null : createId.trim();
    }

    
    public Date getCreateTime() {
        return createTime;
    }

    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    
    public String getUpdateId() {
        return updateId;
    }

    
    public void setUpdateId(String updateId) {
        this.updateId = updateId == null ? null : updateId.trim();
    }

    
    public Date getUpdateTime() {
        return updateTime;
    }

    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    
    public String getIdCard() {
        return idCard;
    }

    
    public void setIdCard(String idCard) {
        this.idCard = idCard == null ? null : idCard.trim();
    }
}