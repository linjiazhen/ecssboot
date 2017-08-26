package com.wintoo.model;

public class EnergySearch {
	private String energytypeid;
	private String energytype;
	private String model;
	private String modellevel;
	private String modelid;
	private String startdate;
	private String enddate;
	private String basetime;
	private String caltype;
	private String showtype;
	public String getEnergytypeid() {
		return energytypeid;
	}
	public void setEnergytypeid(String energytypeid) {
		this.energytypeid = energytypeid;
	}
	public String getEnergytype() {
		return energytype;
	}
	public void setEnergytype(String energytype) {
		this.energytype = energytype;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getModelid() {
		return modelid;
	}
	public void setModelid(String modelid) {
		this.modelid = modelid;
	}
	public String getBasetime() {
		return basetime;
	}
	public void setBasetime(String basetime) {
		this.basetime = basetime;
	}
	public String getCaltype() {
		return caltype;
	}
	public void setCaltype(String caltype) {
		this.caltype = caltype;
	}
	public String getModellevel() {
		return modellevel;
	}
	public void setModellevel(String modellevel) {
		this.modellevel = modellevel;
	}

	public void setShowtype(String showtype) {
		this.showtype = showtype;
	}

	public String getShowtype() {
		return showtype;
	}

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }
}
