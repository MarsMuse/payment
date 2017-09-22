package com.zhph.manager.model;

import java.io.Serializable;



/**
 *
 * Author: Zou Yao
 * Description: (系统菜单)
 * Time: 2017/8/3 15:24
 *
**/
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;   //系统菜单id
    private String parentId;   //父级菜单id
    private String name;   //名称
    private String url;   //url串
    private String icon;   //菜单图标
    private Integer displayOrder;   //同级节点间显示顺序
    private String path;   //菜单路径
    private String note;   //备注
    private Integer locked;   //是否锁定



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
}
