package com.wintoo.model;

import java.util.Date;

public class LeakSearch {
	private String netid;
	private Date   startdate;
	private Date   enddate;
	private String basetime;
    public String getNetid() {
        return netid;
    }

    public void setNetid(String netid) {
        this.netid = netid;
    }
    public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	public String getBasetime() {
		return basetime;
	}
	public void setBasetime(String basetime) {
		this.basetime = basetime;
	}
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
}
