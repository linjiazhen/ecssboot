package com.wintoo.model;

/**
 * Created by Jason on 15/5/18.
 */
public class AlertConf {
    private int isalert;
    private int percent;
    private int alerttype;

    public int getIsalert() {
        return isalert;
    }

    public void setIsalert(int isalert) {
        this.isalert = isalert;
    }

    public int getAlerttype() {
        return alerttype;
    }

    public void setAlerttype(int alerttype) {
        this.alerttype = alerttype;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
