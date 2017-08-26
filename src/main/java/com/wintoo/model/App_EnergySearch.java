package com.wintoo.model;
public class App_EnergySearch {
	//{model:build|organ,modellevel:group|build|floor|room,modelid:model's uuid,energytypeid:energytype's uuid,energytype:energytype's name,startdate:xxxx/xx/xx,enddate:xxxx/xx/xx,basetime:minutes|hour|day|month|year,caltype:total|people|area,showtype:value|yony|chain}}
    private String model;//build|organ
    private String modellevel;//group|build|floor|room
    private String modelid;//model's uuid
	private String energytypeid;//energytype's uuid
	private String energytype;//energytype's name,
	private String startdate;//xxxx/xx/xx
	private String enddate;//xxxx/xx/xx
	private String basetime;//minutes|hour|day|month|year
	private String caltype;//total|people|area
	private String showtype;//showtype:yony|notyony
	
	
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
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getBasetime() {
		return basetime;
	}
	public void setBasetime(String basetime) {
		this.basetime = basetime;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
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
	public String getShowtype() {
		return showtype;
	}
	public void setShowtype(String showtype) {
		this.showtype = showtype;
	}
}
