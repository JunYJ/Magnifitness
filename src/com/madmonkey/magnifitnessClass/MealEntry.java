package com.madmonkey.magnifitnessClass;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

public class MealEntry
{

	private String			mealType;
	private Calendar		date;
	private Double			totalCalorie;
	private ArrayList<Food>	foodList;
	private String			dateStr;

	public MealEntry()
	{
		mealType = "";
		totalCalorie = 0.0;
		foodList = new ArrayList<Food>();
		
		date = Calendar.getInstance();
		
		DateFormatSymbols dfs = new DateFormatSymbols();
		String [] months = dfs.getMonths();
		
		/*setDateStr("" + date.get(Calendar.DAY_OF_MONTH) + "th "
				+ months[(date.get(Calendar.MONTH))] + " " + date.get(Calendar.YEAR));
		*/
		if(date.get(Calendar.DAY_OF_MONTH) == 1 || date.get(Calendar.DAY_OF_MONTH) == 11 
				|| date.get(Calendar.DAY_OF_MONTH) == 21 || date.get(Calendar.DAY_OF_MONTH) == 31)
		{
			setDateStr("" + date.get(Calendar.DAY_OF_MONTH) + "st "
					+ months[(date.get(Calendar.MONTH))] + " " + date.get(Calendar.YEAR));
		}
		else if(date.get(Calendar.DAY_OF_MONTH) == 2 || date.get(Calendar.DAY_OF_MONTH) == 12 || date.get(Calendar.DAY_OF_MONTH) == 22)
		{
			setDateStr("" + date.get(Calendar.DAY_OF_MONTH) + "nd "
					+ months[(date.get(Calendar.MONTH))] + " " + date.get(Calendar.YEAR));
		}
		else if(date.get(Calendar.DAY_OF_MONTH) == 3 || date.get(Calendar.DAY_OF_MONTH) == 13 || date.get(Calendar.DAY_OF_MONTH) == 23)
		{
			setDateStr("" + date.get(Calendar.DAY_OF_MONTH) + "rd "
					+ months[(date.get(Calendar.MONTH))] + " " + date.get(Calendar.YEAR));
		}
		else
		{
			setDateStr("" + date.get(Calendar.DAY_OF_MONTH) + "th "
					+ months[(date.get(Calendar.MONTH))] + " " + date.get(Calendar.YEAR));
		}
	}
	
	public MealEntry(String mealType, Double totalCalorie,
			ArrayList<Food> foodList)
	{
		this.mealType = mealType;
		this.totalCalorie = totalCalorie;
		this.foodList = foodList;
		// Date incomplete
		date = Calendar.getInstance();
		
		DateFormatSymbols dfs = new DateFormatSymbols();
		String [] months = dfs.getMonths();
		
		setDateStr("" + date.get(Calendar.DAY_OF_MONTH) + "th "
				+ months[(date.get(Calendar.MONTH))] + " " + date.get(Calendar.YEAR));

	}

	public String getMealType()
	{
		return mealType;
	}

	public Calendar getDate()
	{
		return date;
	}

	public Double getTotalCalorie()
	{
		return totalCalorie;
	}

	public ArrayList<Food> getFoodList()
	{
		return foodList;
	}

	public void setMealType(String mealType)
	{
		this.mealType = mealType;
	}

	public void setDate(Calendar date)
	{
		this.date = date;
	}

	public void setTotalCalorie(Double totalCalorie)
	{
		this.totalCalorie = totalCalorie;
	}

	public void setFoodList(ArrayList<Food> foodList)
	{
		this.foodList = foodList;
	}

	public String getDateStr()
	{
		return dateStr;
	}

	public void setDateStr(String dateStr)
	{
		this.dateStr = dateStr;
	}

}
