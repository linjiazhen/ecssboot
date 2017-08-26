package com.wintoo.model;

/**
 * Created by Jason on 15/5/26.
 */
public class PublicInfo {
    private String buildcode;
    private String buildname;
    private String func;
    private double totalenergy;
    private double areaenergy;
    private double peopleenergy;
    private double totalwater;
    private double areawater;
    private double peoplewater;

    public void setAreaenergy(double areaenergy) {
        this.areaenergy = areaenergy;
    }

    public void setAreawater(double areawater) {
        this.areawater = areawater;
    }

    public void setBuildcode(String buildcode) {
        this.buildcode = buildcode;
    }

    public void setBuildname(String buildname) {
        this.buildname = buildname;
    }

    public void setPeopleenergy(double peopleenergy) {
        this.peopleenergy = peopleenergy;
    }

    public void setPeoplewater(double peoplewater) {
        this.peoplewater = peoplewater;
    }

    public void setTotalenergy(double totalenergy) {
        this.totalenergy = totalenergy;
    }

    public void setTotalwater(double totalwater) {
        this.totalwater = totalwater;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public double getAreaenergy() {
        return areaenergy;
    }

    public double getAreawater() {
        return areawater;
    }

    public double getPeopleenergy() {
        return peopleenergy;
    }

    public double getPeoplewater() {
        return peoplewater;
    }

    public double getTotalenergy() {
        return totalenergy;
    }

    public double getTotalwater() {
        return totalwater;
    }

    public String getBuildcode() {
        return buildcode;
    }

    public String getBuildname() {
        return buildname;
    }

    public String getFunc() {
        return func;
    }
}
