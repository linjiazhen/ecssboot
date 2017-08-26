package com.wintoo.model;

/**
 * Created by HaiziGe on 15/9/29.
 */
public class PublicBulidsInfo {
    private String bulidid;
    private String bulid;
    private double totalenergy;
    private double zmczenergy;
    private double ktydenergy;
    private double dlydenergy;
    private double tsydenergy;
    private double bdxhenergy;

    public void setzmczenergy(double zmczenergy) {        this.zmczenergy = zmczenergy;    }

    public void settsydenergy(double tsydenergy) {        this.tsydenergy = tsydenergy;    }

    public void setbulidid(String bulidid) {        this.bulidid = bulidid;    }

    public void setbulid(String bulid) {        this.bulid = bulid;    }

    public void setktydenergy(double ktydenergy) {        this.ktydenergy = ktydenergy;    }

    public void setbdxhenergy(double bdxhenergy) {        this.bdxhenergy = bdxhenergy;    }

    public void setTotalenergy(double totalenergy) {        this.totalenergy = totalenergy;    }

    public void setdlydenergy(double dlydenergy) {        this.dlydenergy = dlydenergy;    }

    public double getzmczenergy() {        return zmczenergy;    }

    public double gettsydenergy() {        return tsydenergy;    }

    public double getktydenergy() {        return ktydenergy;    }

    public double getbdxhenergy() {        return bdxhenergy;    }

    public double getTotalenergy() {        return totalenergy;    }

    public double getdlydenergy() {        return dlydenergy;    }

    public String getbulidid() {        return bulidid;    }

    public String getbulid() {        return bulid;    }
}
