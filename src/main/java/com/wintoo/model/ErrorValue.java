package com.wintoo.model;

/**
 * Created by Jason on 15/10/30.
 */
public class ErrorValue {
    private String time;
    private double oldvalue;
    private double value;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public double getOldvalue() {
        return oldvalue;
    }

    public void setOldvalue(double oldvalue) {
        this.oldvalue = oldvalue;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
