package com.zsyc.warehouse.service;

public class Model {

	private String name;
	private int num;
	private String unit;


	public Model(int num, String unit) {
		super();
		this.num = num;
		this.unit = unit;
	}



	public Model(String name, int num, String unit) {
		super();
		this.name = name;
		this.num = num;
		this.unit = unit;
	}



	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


}
