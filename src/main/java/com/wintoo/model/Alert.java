package com.wintoo.model;

/**
 * Created by Jason on 15/5/18.
 */
public class Alert {
    private String uuid;
    private String first;
    private String second;
    private String third;
    private String energyitem;
    private String alerttime;
    private String timeunit;
    private String datetime;
    private double value;
    private double usage;
    private int    status;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getThird() {
        return third;
    }

    public void setThird(String third) {
        this.third = third;
    }

    public String getEnergyitem() {
        return energyitem;
    }

    public void setEnergyitem(String energyitem) {
        this.energyitem = energyitem;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getUsage() {
        return usage;
    }

    public void setUsage(double usage) {
        this.usage = usage;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTimeunit() {
        return timeunit;
    }

    public void setTimeunit(String timeunit) {
        this.timeunit = timeunit;
    }

    public String getAlerttime() {
        return alerttime;
    }

    public void setAlerttime(String alerttime) {
        this.alerttime = alerttime;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
