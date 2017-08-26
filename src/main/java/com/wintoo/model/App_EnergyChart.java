package com.wintoo.model;
import java.util.List;

public class App_EnergyChart {
	private List<String> modelid;//建筑/机构uuid
	private List<String> modelname;//建筑/机构名称
	private List<Double> energy;//能耗数据
	private List<Double> chain;//同比系数
	private List<Double> yony;//环比系数
	private String energytype;//能耗类型
	private List<Double> rated;//能耗额定值,数据库中额定值都是指月额定值，具体计算的时候再进行换算成年、日,一般不做这么复杂
	private List<String> datatime;//数据时间格式先统一为 YYYY/MM/DD H24:MI，具体需要什么的可以在底层或者js展示时截取

	public String getEnergytype() {
		return energytype;
	}
	public void setEnergytype(String energytype) {
		this.energytype = energytype;
	}
	public List<String> getDatatime() {
		return datatime;
	}
	public void setDatatime(List<String> datatime) {
		this.datatime = datatime;
	}

	
	public List<Double> getEnergy() {
		return energy;
	}
	public void setEnergy(List<Double> energy) {
		this.energy = energy;
	}
	public List<Double> getChain() {
		return chain;
	}
	public void setChain(List<Double> chain) {
		this.chain = chain;
	}
	public List<Double> getYony() {
		return yony;
	}
	public void setYony(List<Double> yony) {
		this.yony = yony;
	}
	public List<String> getModelid() {
		return modelid;
	}
	public void setModelid(List<String> modelid) {
		this.modelid = modelid;
	}
	public List<String> getModelname() {
		return modelname;
	}
	public void setModelname(List<String> modelname) {
		this.modelname = modelname;
	}
	public List<Double> getRated() {
		return rated;
	}
	public void setRated(List<Double> rated) {
		this.rated = rated;
	}
}

