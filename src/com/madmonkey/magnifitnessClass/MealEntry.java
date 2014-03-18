package com.madmonkey.magnifitnessClass;

import java.util.ArrayList;
import java.util.Date;

public class MealEntry {

	private String			mealType;
	private Date			date;
	private Double			totalCalorie;
	private ArrayList<Food>	foodList;

	public MealEntry(String mealType, Double totalCalorie, ArrayList<Food> foodList)
		{
		this.mealType = mealType;
		this.totalCalorie = totalCalorie;
		this.foodList = foodList;
		// Date incomplete

		}

	public String getMealType()
		{
		return mealType;
		}

	public Date getDate()
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

	public void setDate(Date date)
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

}
