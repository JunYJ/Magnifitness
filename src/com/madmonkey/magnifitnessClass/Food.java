package com.madmonkey.magnifitnessClass;

public class Food
{
	int		id;
	String	title;
	String	measurementUnit;
	double	calorie;
	String	type;
	int counter = 1;
	int numOfEntry;
	
	public Food()
	{
		title = "";
		measurementUnit = "";
		calorie = 0.0;
		type = "";
		id = counter;
		counter++;
		numOfEntry = 0;
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
		numOfEntry = 0;
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
	
	public int getNumOfEntry()
	{
		return numOfEntry;
	}

	public void setNumOfEntry(int numOfEntry)
	{
		this.numOfEntry = numOfEntry;
	}

	public String toString()
	{
		return "Title: " + getTitle() + "Measurement Unit: " + getMeasurementUnit() + "Calorie: " + getCalorie() + "Type: " + getType();
	}

}
