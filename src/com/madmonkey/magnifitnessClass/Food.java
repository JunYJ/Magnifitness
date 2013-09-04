package com.madmonkey.magnifitnessClass;

public class Food
{
	String title;
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMeasurementUnit() {
		return measurementUnit;
	}

	public void setMeasurementUnit(String measurementUnit) {
		this.measurementUnit = measurementUnit;
	}

	public int getCalorie() {
		return calorie;
	}

	public void setCalorie(int calorie) {
		this.calorie = calorie;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	String measurementUnit;
	int calorie;
	String type;
	
	public Food(String titleIn, String measurementUnitIn, int calorieIn, String typeIn)
	{
		title = titleIn;
		measurementUnit = measurementUnitIn;
		calorie = calorieIn;
		type = typeIn;
	}
	
	
}
