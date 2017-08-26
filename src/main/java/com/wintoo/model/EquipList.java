package com.wintoo.model;

public class EquipList {
	private String uuid;
	private String equipid;
	private String batch;
	private String batchid;
	private String typeid;
	private String type;
	private String subtype;
	private String model;
	private String installtype;
	private String groupid;
	private String buildid;
	private String floorid;
	private String roomid;
	private double longitude;
	private double latitude;
	private String longitudes;
	private String latitudes;
	private String remark;
	private String remarkinfo;
	private int    maintenance;
	private String paras;//所属网关-启用状态-节点编号
    private int    connectnum;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getEquipid() {
		return equipid;
	}
	public void setEquipid(String equipid) {
		this.equipid = equipid;
	}
	public String getBatchid() {
		return batchid;
	}
	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}
	public String getInstalltype() {
		return installtype;
	}
	public void setInstalltype(String installtype) {
		this.installtype = installtype;
	}
	public String getFloorid() {
		return floorid;
	}
	public void setFloorid(String floorid) {
		this.floorid = floorid;
	}
	public String getBuildid() {
		return buildid;
	}
	public void setBuildid(String buildid) {
		this.buildid = buildid;
	}
	public String getRoomid() {
		return roomid;
	}
	public void setRoomid(String roomid) {
		this.roomid = roomid;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubtype() {
		return subtype;
	}
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getTypeid() {
		return typeid;
	}
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
	public String getLongitudes() {
		return longitudes;
	}
	public void setLongitudes(String longitudes) {
		this.longitudes = longitudes;
	}
	public String getLatitudes() {
		return latitudes;
	}
	public void setLatitudes(String latitudes) {
		this.latitudes = latitudes;
	}
	public int getMaintenance() {
		return maintenance;
	}
	public void setMaintenance(int maintenance) {
		this.maintenance = maintenance;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String getParas() {
		return paras;
	}
	public void setParas(String paras) {
		this.paras = paras;
	}

	public void setRemarkinfo(String remarkinfo) {
		this.remarkinfo = remarkinfo;
	}

	public String getRemarkinfo() {
		return remarkinfo;
	}

    public int getConnectnum() {
        return connectnum;
    }

    public void setConnectnum(int connectnum) {
        this.connectnum = connectnum;
    }
}
