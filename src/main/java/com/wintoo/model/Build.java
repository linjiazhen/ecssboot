package com.wintoo.model;

public class Build {
	private String id;
	private String code;
	private String name;
	private String groupid;
	private String group;
	private String buildtypeid;
	private String buildtype;
	private double area;
    private int    people;
	private double airarea;
	private int year;
	private int upfloor;
	private int downfloor;
	private int zerofloor;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getBuildtype() {
		return buildtype;
	}

	public void setBuildtype(String buildtype) {
		this.buildtype = buildtype;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public double getAirarea() {
		return airarea;
	}

	public void setAirarea(double airarea) {
		this.airarea = airarea;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getUpfloor() {
		return upfloor;
	}

	public void setUpfloor(int upfloor) {
		this.upfloor = upfloor;
	}

	public int getDownfloor() {
		return downfloor;
	}

	public void setDownfloor(int downfloor) {
		this.downfloor = downfloor;
	}

	public String getBuildtypeid() {
		return buildtypeid;
	}

	public void setBuildtypeid(String buildtypeid) {
		this.buildtypeid = buildtypeid;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public int getZerofloor() {
		return zerofloor;
	}

	public void setZerofloor(int zerofloor) {
		this.zerofloor = zerofloor;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }
}
