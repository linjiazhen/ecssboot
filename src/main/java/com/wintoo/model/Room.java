package com.wintoo.model;

public class Room {
	private String id;
	private String name;
	private String floorid;
	private String roomtypeid;
	private int    people;
	private double area;
	private int    roomnum;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFloorid() {
		return floorid;
	}
	public void setFloorid(String floorid) {
		this.floorid = floorid;
	}
	public String getRoomtypeid() {
		return roomtypeid;
	}
	public void setRoomtypeid(String roomtypeid) {
		this.roomtypeid = roomtypeid;
	}

	public double getArea() {
		return area;
	}
	public void setArea(double area) {
		this.area = area;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getRoomnum() {
		return roomnum;
	}
	public void setRoomnum(int roomnum) {
		this.roomnum = roomnum;
	}

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }
}
