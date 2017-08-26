package com.wintoo.model;

public class App_timesplitenergy {
	private String builduuid;
	private String buildname;
	private String times;
	private int    electricity;
	private int    ratedelec;
	private int    water;
	private int    ratedwater;
	
	private int    energy;
	private int    rated;
	private int    rownumber;
	
	
	public int getElectricity() {
		return electricity;
	}
	public void setElectricity(int electricity) {
		this.electricity = electricity;
	}
	public int getWater() {
		return water;
	}
	public void setWater(int water) {
		this.water = water;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	public String getBuildname() {
		return buildname;
	}
	public void setBuildname(String buildname) {
		this.buildname = buildname;
	}
	public String getBuilduuid() {
		return builduuid;
	}
	public void setBuilduuid(String builduuid) {
		this.builduuid = builduuid;
	}
	public int getRatedelec() {
		return ratedelec;
	}
	public void setRatedelec(int ratedelec) {
		this.ratedelec = ratedelec;
	}
	public int getRatedwater() {
		return ratedwater;
	}
	public void setRatedwater(int ratedwater) {
		this.ratedwater = ratedwater;
	}
	public int getEnergy() {
		return energy;
	}
	public void setEnergy(int energy) {
		this.energy = energy;
	}
	public int getRated() {
		return rated;
	}
	public void setRated(int rated) {
		this.rated = rated;
	}
	public int getRownumber() {
		return rownumber;
	}
	public void setRownumber(int rownumber) {
		this.rownumber = rownumber;
	}
	

}
