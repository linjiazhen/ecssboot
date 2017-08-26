package com.wintoo.model;

public class WatermeterData {
	private String id;
	private String datatime;
	private double u;
	private double consum;
	private double flow;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDatatime() {
		return datatime;
	}
	public void setDatatime(String datatime) {
		this.datatime = datatime;
	}
	public double getU() {
		return u;
	}
	public void setU(double u) {
		this.u = u;
	}
	public double getConsum() {
		return consum;
	}
	public void setConsum(double consum) {
		this.consum = consum;
	}
	public double getFlow() {
		return flow;
	}
	public void setFlow(double flow) {
		this.flow = flow;
	}
}
