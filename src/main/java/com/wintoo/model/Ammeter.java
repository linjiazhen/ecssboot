package com.wintoo.model;

public class Ammeter {
	private String uuid;
	private String equip;
	private String batch;
	private String type;
	private String subtype;
	private String model;
	private int    installtype;
	private String group;
	private String build;
	private String floor;
	private String room;
	private String remark;
	private String longitude;
	private String latitude;
	private String gateway;
	private String gatewayaddress;
	private String gatewayid;
	private int pn;
	private int number;
	private int speed;
	private int port;
	private int protocol;
    private int cut;
	private String address;
	private String password;
	private int    ratecount=99999999;
	private int    dataformat=99999999;
	private int	   classnumber=99999999;
	private int    remote;
	private int    use;
	private int    on_off;
	private String typeid;
	private double newest_data;
	private String newest_data_time;
	private String newest_operate_time;
    private double newest_valid_data;
    private String newest_valid_data_time;
	private int    status;
	private String last_15;
	private String last_day;
	private String last_mon;
	private String remarkinfo;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public int getUse() {
		return use;
	}
	public void setUse(int use) {
		this.use = use;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
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
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getEquip() {
		return equip;
	}
	public void setEquip(String equip) {
		this.equip = equip;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public int getInstalltype() {
		return installtype;
	}
	public void setInstalltype(int installtype) {
		this.installtype = installtype;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public void setGatewayaddress(String gatewayaddress) {
		this.gatewayaddress = gatewayaddress;
	}

	public String getGatewayaddress() {
		return gatewayaddress;
	}

	public void setGatewayid(String gatewayid) {
		this.gatewayid = gatewayid;
	}

	public String getGatewayid() {
		return gatewayid;
	}

	public int getPn() {
		return pn;
	}
	public void setPn(int pn) {
		this.pn = pn;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getProtocol() {
		return protocol;
	}
	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getRatecount() {
		return ratecount;
	}
	public void setRatecount(int ratecount) {
		this.ratecount = ratecount;
	}
	public int getDataformat() {
		return dataformat;
	}
	public void setDataformat(int dataformat) {
		this.dataformat = dataformat;
	}
	public int getClassnumber() {
		return classnumber;
	}
	public void setClassnumber(int classnumber) {
		this.classnumber = classnumber;
	}
	public int getRemote() {
		return remote;
	}
	public void setRemote(int remote) {
		this.remote = remote;
	}
	public int getOn_off() {
		return on_off;
	}
	public void setOn_off(int on_off) {
		this.on_off = on_off;
	}
	public String getTypeid() {
		return typeid;
	}
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
	public Double getNewest_data() {
		return newest_data;
	}
	public void setNewest_data(Double newest_data) {
		this.newest_data = newest_data;
	}
	public String getNewest_data_time() {
		return newest_data_time;
	}
	public void setNewest_data_time(String newest_data_time) {
		this.newest_data_time = newest_data_time;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getNewest_operate_time() {
		return newest_operate_time;
	}
	public void setNewest_operate_time(String newest_operate_time) {
		this.newest_operate_time = newest_operate_time;
	}
	public String getLast_15() {
		return last_15;
	}
	public void setLast_15(String last_15) {
		this.last_15 = last_15;
	}
	public String getLast_day() {
		return last_day;
	}
	public void setLast_day(String last_day) {
		this.last_day = last_day;
	}
	public String getLast_mon() {
		return last_mon;
	}
	public void setLast_mon(String last_mon) {
		this.last_mon = last_mon;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	public void setRemarkinfo(String remarkinfo) {
		this.remarkinfo = remarkinfo;
	}

	public String getRemarkinfo() {
		return remarkinfo;
	}


    public String getNewest_valid_data_time() {
        return newest_valid_data_time;
    }

    public void setNewest_valid_data_time(String newest_valid_data_time) {
        this.newest_valid_data_time = newest_valid_data_time;
    }

    public double getNewest_valid_data() {
        return newest_valid_data;
    }

    public void setNewest_valid_data(double newest_valid_data) {
        this.newest_valid_data = newest_valid_data;
    }

    public int getCut() {
        return cut;
    }

    public void setCut(int cut) {
        this.cut = cut;
    }
}
