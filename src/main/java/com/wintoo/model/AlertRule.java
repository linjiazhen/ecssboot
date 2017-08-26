package com.wintoo.model;

/**
 * Created by Jason on 15/5/18.
 */
public class AlertRule {
    private String uuid;
    private String first;
    private String firstid;
    private String second;
    private String secondid;
    private String third;
    private String thirdid;
    private String organlevel;
    private String energyitem;
    private String energyitemcode;
    private String timeunit;
    private String time;
    private double value;
    private int    state;


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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getOrganlevel() {
        return organlevel;
    }

    public void setOrganlevel(String organlevel) {
        this.organlevel = organlevel;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTimeunit() {
        return timeunit;
    }

    public void setTimeunit(String timeunit) {
        this.timeunit = timeunit;
    }

    public String getFirstid() {
        return firstid;
    }

    public void setFirstid(String firstid) {
        this.firstid = firstid;
    }

    public String getSecondid() {
        return secondid;
    }

    public void setSecondid(String secondid) {
        this.secondid = secondid;
    }

    public String getThirdid() {
        return thirdid;
    }

    public void setThirdid(String thirdid) {
        this.thirdid = thirdid;
    }

    public String getEnergyitemcode() {
        return energyitemcode;
    }

    public void setEnergyitemcode(String energyitemcode) {
        this.energyitemcode = energyitemcode;
    }
}
