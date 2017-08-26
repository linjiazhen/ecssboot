package com.wintoo.model;

public class Floor {
	private String id;
	private int code;
	private String name;
	private String buildid;
    private double area;
    private int    people;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBuildid() {
		return buildid;
	}
	public void setBuildid(String buildid) {
		this.buildid = buildid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }
}
