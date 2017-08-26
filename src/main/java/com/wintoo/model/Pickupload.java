package com.wintoo.model;

/**
 * Created by Jason on 15/10/27.
 */
public class Pickupload {
    private String datatime;
    private int    pickstatus;
    private int    uploadstatus;
    private String operatetime;
    private String filename;

    public int getPickstatus() {
        return pickstatus;
    }

    public void setPickstatus(int pickstatus) {
        this.pickstatus = pickstatus;
    }

    public int getUploadstatus() {
        return uploadstatus;
    }

    public void setUploadstatus(int uploadstatus) {
        this.uploadstatus = uploadstatus;
    }

    public String getOperatetime() {
        return operatetime;
    }

    public void setOperatetime(String operatetime) {
        this.operatetime = operatetime;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDatatime() {
        return datatime;
    }

    public void setDatatime(String datatime) {
        this.datatime = datatime;
    }
}
