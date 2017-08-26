package com.wintoo.model;

import java.util.List;

public class EnergyChart {
	private Energy energy;
	private List<String> categories;
	private Energy chainenergy;
	private Energy yonyenergy;
	public Energy getEnergy() {
		return energy;
	}
	public void setEnergy(Energy energy) {
		this.energy = energy;
	}
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	public Energy getChainenergy() {
		return chainenergy;
	}
	public void setChainenergy(Energy chainenergy) {
		this.chainenergy = chainenergy;
	}
	public Energy getYonyenergy() {
		return yonyenergy;
	}
	public void setYonyenergy(Energy yonyenergy) {
		this.yonyenergy = yonyenergy;
	}
	
}
