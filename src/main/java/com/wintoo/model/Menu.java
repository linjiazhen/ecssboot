package com.wintoo.model;

public class Menu {
	private String uuid;
    private String code;
	private String name;
	private String levels;
	private String upmenu;
	private String upmenuid;
	private String pagename;
	private String image;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUpmenu() {
		return upmenu;
	}

	public void setUpmenu(String upmenu) {
		this.upmenu = upmenu;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPagename() {
		return pagename;
	}

	public void setPagename(String pagename) {
		this.pagename = pagename;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLevels() {
		return levels;
	}

	public void setLevels(String levels) {
		this.levels = levels;
	}

	public String getUpmenuid() {
		return upmenuid;
	}

	public void setUpmenuid(String upmenuid) {
		this.upmenuid = upmenuid;
	}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
