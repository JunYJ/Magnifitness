package com.madmonkey.magnifitnessClass;

public class User 
{
	String name;
	int age;
	String email;
	String gender;
	double bmr;
	int currentWeight;
	int height;
	int idealWeight;
	
	public User()
	{
		name = "";
		age = 0;
		email = "";
		gender = "";
		bmr = 0.0;
		currentWeight = 0;
		height = 0;
		idealWeight = 0;
	}
	
	public User(String nameIn, int ageIn, String emailIn, String genderIn, int currentWeightIn, int heightIn)
	{
		name = nameIn;
		age = ageIn;
		gender = genderIn;
		email = emailIn;
		currentWeight = currentWeightIn;
		height = heightIn;
	}
	
	public void setName(String nameIn)
	{
		name = nameIn;
	}
	
	public void setAge(int ageIn)
	{
		age = ageIn;
	}
	
	public void setEmail(String emailIn)
	{
		email = emailIn;
	}
	
	public void setBmr(double bmrIn)
	{
		bmr = bmrIn;
	}
	
	public void setCurrentWeight(int currentWeightIn)
	{
		currentWeight = currentWeightIn;
	}
	
	public void setHeight(int heightIn)
	{
		height = heightIn;
	}
	
	public void setIdealWeight(int idealWeightIn)
	{
		idealWeight = idealWeightIn;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getAge()
	{
		return age;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public String getGender()
	{
		return gender;
	}
	
	public double getBmr()
	{
		return bmr;
	}
	
	public int getCurrentWeight()
	{
		return currentWeight;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getIdealWeight()
	{
		return idealWeight;
	}
	
	public void setUser(String nameIn, int ageIn, String emailIn, String genderIn, int currentWeightIn, int heightIn)
	{
		name = nameIn;
		age = ageIn;
		gender = genderIn;
		email = emailIn;
		currentWeight = currentWeightIn;
		height = heightIn;
	}
}
