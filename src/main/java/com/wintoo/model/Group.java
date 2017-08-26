package com.wintoo.model;

public class Group {
	private String id;
	private String name;
	private String aliasname;
	private String desc;
	private String code;
    private double area;
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
	public String getAliasname() {
		return aliasname;
	}
	public void setAliasname(String aliasname) {
		this.aliasname = aliasname;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }
}
