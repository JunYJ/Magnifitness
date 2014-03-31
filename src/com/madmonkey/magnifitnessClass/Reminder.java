package com.madmonkey.magnifitnessClass;

import java.util.Date;

public class Reminder {

	private String		mealType;
	private Date		date;
	private boolean		status;

	final static String	BREAKFAST	= "breakfast";
	final static String	LUNCH		= "lunch";
	final static String	SNACK		= "snack";
	final static String	DINNER		= "dinner";


	public Reminder()
		{
		mealType = "";
		date = null;
		status = false;
		}
	
	public Reminder(String mealType, Date inDate) {
		this.mealType = mealType;
		this.date = inDate;
		this.status = false;
	
	}

	public String getMeal()
		{
		return mealType;
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
		mealType = mealIn;
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
