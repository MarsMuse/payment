package com.zhph.base.entity;



/**
 *
 * Author: Zou Yao
 * Description: (系统服务Bean)
 * Time: 2017/8/4 9:31
 *
**/
public class Service {
    private static final long serialVersionUID = 1L;

    private String id;   //系统菜单id
    private String name;   //名称
    private String bean;   //bean名称
    private String method;   //方法名称
    private String url;   //url串
    private String note;   //备注
    private Integer locked;   //是否锁定
    private Integer reserved;    //系统保留



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getLocked() {
        return locked;
    }

    public void setLocked(Integer locked) {
        this.locked = locked;
    }

    public Integer getReserved() {
        return reserved;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }
}
