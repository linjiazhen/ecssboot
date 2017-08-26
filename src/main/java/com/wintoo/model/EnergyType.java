package com.wintoo.model;

public class EnergyType {
	private int itemlevel;
	private String itemcode;
	private String itemname;
	private String parentitemcode;
	private String parentitemname;
	private String itemtype;
	private String itemunit;
	private double itemfml;
	private int itemstate;
	public String getItemcode() {
		return itemcode;
	}
	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public String getParentitemcode() {
		return parentitemcode;
	}
	public void setParentitemcode(String parentitemcode) {
		this.parentitemcode = parentitemcode;
	}
	public String getParentitemname() {
		return parentitemname;
	}
	public void setParentitemname(String parentitemname) {
		this.parentitemname = parentitemname;
	}
	public String getItemtype() {
		return itemtype;
	}
	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}
	public String getItemunit() {
		return itemunit;
	}
	public void setItemunit(String itemunit) {
		this.itemunit = itemunit;
	}
	public int getItemstate() {
		return itemstate;
	}
	public void setItemstate(int itemstate) {
		this.itemstate = itemstate;
	}
	public int getItemlevel() {
		return itemlevel;
	}
	public void setItemlevel(int itemlevel) {
		this.itemlevel = itemlevel;
	}


    public double getItemfml() {
        return itemfml;
    }

    public void setItemfml(double itemfml) {
        this.itemfml = itemfml;
    }
}
	

