package com.madmonkey.magnifitnessClass;

public class Food
{
	int		id;
	String	title;
	String	measurementUnit;
	double	calorie;
	String	type;
	int counter = 1;
	
	public Food()
	{
		title = "";
		measurementUnit = "";
		calorie = 0.0;
		type = "";
		id = counter;
		counter++;
	}

	public Food(String titleIn, String measurementUnitIn, double calorieIn,
			String typeIn)
	{
		title = titleIn;
		measurementUnit = measurementUnitIn;
		calorie = calorieIn;
		type = typeIn;
		id = counter;
		counter++;
	}
	
	public int getId()
	{
		return id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getMeasurementUnit()
	{
		return measurementUnit;
	}

	public void setMeasurementUnit(String measurementUnit)
	{
		this.measurementUnit = measurementUnit;
	}

	public double getCalorie()
	{
		return calorie;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}

	public void setCalorie(double calorie)
	{
		this.calorie = calorie;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

}
