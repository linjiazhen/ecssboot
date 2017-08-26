package com.wintoo.model;

public class Gateway {
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
	private String address;
	private String code;
	private String ipmain;
	private String portmain;
	private String ipbackup;
	private String portbackup;
	private String apn;
	private int    delay1 = 99999999;
	private int    delay2 = 99999999;
	private int	   waittime = 99999999;
	private int    flag = 99999999;
	private int    heartbeat = 99999999;
	private int    use;
	private int    state;
	private String newest_hearbeat_time;
	private String login_time;
	private String lost_connet_time;
	private String gateway_ver;
	private String zd_ip;
	private String zd_mask;
	private String zd_gateway;
	private String zd_mac;
	private String zd_lcd_password;
	private int    server_flag;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getIpmain() {
		return ipmain;
	}
	public void setIpmain(String ipmain) {
		this.ipmain = ipmain;
	}
	public String getPortmain() {
		return portmain;
	}
	public void setPortmain(String portmain) {
		this.portmain = portmain;
	}
	public String getIpbackup() {
		return ipbackup;
	}
	public void setIpbackup(String ipbackup) {
		this.ipbackup = ipbackup;
	}
	public String getPortbackup() {
		return portbackup;
	}
	public void setPortbackup(String portbackup) {
		this.portbackup = portbackup;
	}
	public String getApn() {
		return apn;
	}
	public void setApn(String apn) {
		this.apn = apn;
	}
	public int getDelay1() {
		return delay1;
	}
	public void setDelay1(int delay1) {
		this.delay1 = delay1;
	}
	public int getDelay2() {
		return delay2;
	}
	public void setDelay2(int delay2) {
		this.delay2 = delay2;
	}
	public int getWaittime() {
		return waittime;
	}
	public void setWaittime(int waittime) {
		this.waittime = waittime;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getHeartbeat() {
		return heartbeat;
	}
	public void setHeartbeat(int heartbeat) {
		this.heartbeat = heartbeat;
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
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getNewest_hearbeat_time() {
		return newest_hearbeat_time;
	}
	public void setNewest_hearbeat_time(String newest_hearbeat_time) {
		this.newest_hearbeat_time = newest_hearbeat_time;
	}
	public String getLogin_time() {
		return login_time;
	}
	public void setLogin_time(String login_time) {
		this.login_time = login_time;
	}
	public String getLost_connet_time() {
		return lost_connet_time;
	}
	public void setLost_connet_time(String lost_connet_time) {
		this.lost_connet_time = lost_connet_time;
	}
	public String getGateway_ver() {
		return gateway_ver;
	}
	public void setGateway_ver(String gateway_ver) {
		this.gateway_ver = gateway_ver;
	}
	public String getZd_ip() {
		return zd_ip;
	}
	public void setZd_ip(String zd_ip) {
		this.zd_ip = zd_ip;
	}
	public String getZd_mask() {
		return zd_mask;
	}
	public void setZd_mask(String zd_mask) {
		this.zd_mask = zd_mask;
	}
	public String getZd_gateway() {
		return zd_gateway;
	}
	public void setZd_gateway(String zd_gateway) {
		this.zd_gateway = zd_gateway;
	}
	public String getZd_mac() {
		return zd_mac;
	}
	public void setZd_mac(String zd_mac) {
		this.zd_mac = zd_mac;
	}
	public String getZd_lcd_password() {
		return zd_lcd_password;
	}
	public void setZd_lcd_password(String zd_lcd_password) {
		this.zd_lcd_password = zd_lcd_password;
	}

	public int getServer_flag() {
		return server_flag;
	}

	public void setServer_flag(int server_flag) {
		this.server_flag = server_flag;
	}
}
