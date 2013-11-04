package com.madmonkey.magnifitnessClass;

import java.util.*;

public class Reminder {
	
	private String meal;
	private Date date;
	private boolean status;

	public Reminder() 
	{
		meal = "";
		date = null;
		status = false;
	}
	
	public String getMeal()
	{
		return meal;
	}
	
	public Date getDate()
	{
		return date;
	}
	
	public boolean getStatus()
	{
		return status;
	}
	
	public void setMeal(String mealIn)
	{
		meal = mealIn;
	}
	
	public void setDate(Date dateIn)
	{
		date = dateIn;
	}
	
	public void setStatus(boolean statusIn)
	{
		status = statusIn;
	}

}
