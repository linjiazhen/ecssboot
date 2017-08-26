package com.wintoo.model;

import java.util.List;

/**
 * Created by Jason on 15/7/24.
 */
public class UserMenu implements Comparable<UserMenu>{
    private String image;
    private String txt;
    private String id;
    private String code;
    private List<UserMenu> submenu;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<UserMenu> getSubmenu() {
        return submenu;
    }

    public void setSubmenu(List<UserMenu> submenu) {
        this.submenu = submenu;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public int compareTo(UserMenu userMenu) {
        return this.getCode().compareTo(userMenu.getCode());
    }
}