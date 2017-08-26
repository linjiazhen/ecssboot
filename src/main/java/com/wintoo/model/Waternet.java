package com.wintoo.model;

/**
 * Created by Jason on 15/6/2.
 */
public class Waternet {
    private String uuid;
    private String code;
    private String name;
    private String level;
    private int    pronum;
    private int    connum;
    private double leak;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getPronum() {
        return pronum;
    }

    public void setPronum(int pronum) {
        this.pronum = pronum;
    }

    public int getConnum() {
        return connum;
    }

    public void setConnum(int connum) {
        this.connum = connum;
    }

    public double getLeak() {
        return leak;
    }

    public void setLeak(double leak) {
        this.leak = leak;
    }
}
