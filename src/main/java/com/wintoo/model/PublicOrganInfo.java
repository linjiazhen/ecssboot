package com.wintoo.model;

/**
 * Created by HaiziGe on 15/9/29.
 */
public class PublicOrganInfo {
    private String organid;
    private String organ;
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

    public void setorganid(String organid) {
        this.organid = organid;
    }

    public void setorgan(String organ) {
        this.organ = organ;
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

    public String getorganid() {
        return organid;
    }

    public String getorgan() {
        return organ;
    }
}
