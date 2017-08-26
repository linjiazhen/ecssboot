package com.wintoo.model;

import java.util.Date;


public class ParameterSearch {
	private String equipid;
	private Date   datetime;
	private String parameter;
	public String getEquipid() {
		return equipid;
	}
	public void setEquipid(String equipid) {
		this.equipid = equipid;
	}
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	
}
