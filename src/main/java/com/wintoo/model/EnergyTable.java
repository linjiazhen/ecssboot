package com.wintoo.model;

import java.util.List;

public class EnergyTable {
	private String name;
	private List<Double> data;

	public List<Double> getData() {
		return data;
	}
	public void setData(List<Double> data) {
		this.data = data;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
