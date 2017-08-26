package com.wintoo.model;

/**
 * Created by Jason on 15/6/12.
 */
public class Baoguan {
    private String uuid;
    private String equipid;
    private String address;
    private String install;
    private double usage;
    private double gqusage;
    private double longitude;
    private double latitude;
    private String time;

    public void setEquipid(String equipid) {
        this.equipid = equipid;
    }

    public void setGqusage(double gqusage) {
        this.gqusage = gqusage;
    }

    public void setInstall(String install) {
        this.install = install;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUsage(double usage) {
        this.usage = usage;
    }

    public double getGqusage() {
        return gqusage;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getUsage() {
        return usage;
    }

    public String getEquipid() {
        return equipid;
    }

    public String getInstall() {
        return install;
    }

    public String getTime() {
        return time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
