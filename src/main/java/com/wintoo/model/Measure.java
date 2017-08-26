package com.wintoo.model;

public class Measure {
	private String uuid;
	private String devicecode;
	private String energyitemcode;
    private String energyitem;
    private String groupid;
    private String buildid;
    private String floorid;
    private String roomid;
	private String group;
	private String build;
	private String floor;
	private String room;
	private int	   superior_meter_level;
	private String superior_meter;
	private int    percent;
	private int    plusminus;
	private int    level;
	private String remark;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getDevicecode() {
		return devicecode;
	}
	public void setDevicecode(String devicecode) {
		this.devicecode = devicecode;
	}
	public String getEnergyitemcode() {
		return energyitemcode;
	}
	public void setEnergyitemcode(String energyitemcode) {
		this.energyitemcode = energyitemcode;
	}
	public int getPercent() {
		return percent;
	}
	public void setPercent(int percent) {
		this.percent = percent;
	}
	public int getPlusminus() {
		return plusminus;
	}
	public void setPlusminus(int plusminus) {
		this.plusminus = plusminus;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getBuild() {
		return build;
	}
	public void setBuild(String build) {
		this.build = build;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getSuperior_meter_level() {
		return superior_meter_level;
	}
	public void setSuperior_meter_level(int superior_meter_level) {
		this.superior_meter_level = superior_meter_level;
	}
	public String getSuperior_meter() {
		return superior_meter;
	}
	public void setSuperior_meter(String superior_meter) {
		this.superior_meter = superior_meter;
	}

    public String getEnergyitem() {
        return energyitem;
    }

    public void setEnergyitem(String energyitem) {
        this.energyitem = energyitem;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getBuildid() {
        return buildid;
    }

    public void setBuildid(String buildid) {
        this.buildid = buildid;
    }

    public String getFloorid() {
        return floorid;
    }

    public void setFloorid(String floorid) {
        this.floorid = floorid;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }
}
