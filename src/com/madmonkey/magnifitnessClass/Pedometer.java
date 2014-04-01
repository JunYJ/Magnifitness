package com.madmonkey.magnifitnessClass;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class Pedometer
{
	private int step;
	private double distance;
	private Calendar date;
	private String dateStr;
	
	public Pedometer()
	{
		step = 0;
		distance = 0.0;
		date = Calendar.getInstance();
		
		DateFormatSymbols dfs = new DateFormatSymbols();
		String [] months = dfs.getMonths();
		
		setDateStr("" + date.get(Calendar.DAY_OF_MONTH) + "th "
				+ months[(date.get(Calendar.MONTH))] + " " + date.get(Calendar.YEAR));
	}
	
	public Pedometer(String dateStrIn, int stepIn, double distanceIn)
	{
		step = stepIn;
		distance = distanceIn;
		dateStr = dateStrIn;
	}
	
	public int getStep()
	{
		return step;
	}
	public double getDistance()
	{
		return distance;
	}
	public Calendar getDate()
	{
		return date;
	}
	public String getDateStr()
	{
		return dateStr;
	}
	public void setStep(int step)
	{
		this.step = step;
	}
	public void setDistance(double distance)
	{
		this.distance = distance;
	}
	public void setDate(Calendar date)
	{
		this.date = date;
	}
	public void setDateStr(String dateStr)
	{
		this.dateStr = dateStr;
	}
}
