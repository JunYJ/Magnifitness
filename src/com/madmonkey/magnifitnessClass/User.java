package com.madmonkey.magnifitnessClass;

public class User
{
	String name;
	int age;
	String email;
	String gender;
	double bmr;
	double bmi;
	int levelOfActiveness;
	int totalDailyCalorieNeeds;
	int currentWeight;
	int height;
	int idealWeight;

	public User()
	{
		name = "";
		age = 0;
		email = "";
		gender = "";
		levelOfActiveness = 0;
		bmr = 0.0;
		bmi = 0.0;
		currentWeight = 0;
		height = 0;
		idealWeight = 0;
	}

	public User(String nameIn, int ageIn, String emailIn, String genderIn,
			int currentWeightIn, int heightIn, int idealWeightIn)
	{
		name = nameIn;
		age = ageIn;
		gender = genderIn;
		email = emailIn;
		currentWeight = currentWeightIn;
		height = heightIn;
		idealWeight = idealWeightIn;
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

	public void setBmi(double bmiIn)
	{
		bmi = bmiIn;
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

	public double getBmi()
	{
		
		double heightInMeter = getHeight();
		heightInMeter /= 100;

		double heightSquare = heightInMeter * heightInMeter;
		this.bmi = getCurrentWeight() / heightSquare;

		return bmi;
	}

	/**
	 * Metric BMR Formula Women: BMR = 655 + ( 9.6 x weight in kilos ) + ( 1.8 x
	 * height in cm ) - ( 4.7 x age in years ) Men: BMR = 66 + ( 13.7 x weight
	 * in kilos ) + ( 5 x height in cm ) - ( 6.8 x age in years )
	 * 
	 * @return
	 */

	public double getBmr()
	{

		if (gender.equalsIgnoreCase("male"))
			bmr = 66 + (13.7 * getIdealWeight()) + (5 * getHeight())
					- (6.8 * getAge());
		else if (gender.equalsIgnoreCase("female"))
			bmr = 655 + (9.6 * getIdealWeight()) + (1.8 * getHeight())
					- (4.7 * getAge());
		else
			bmr = 0.0;
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

	public int getTotalDailyCalorieNeeds()
	{
		double bmrFactor = 0;
		switch (levelOfActiveness)
		{
			case 0:
				bmrFactor = 1.2; // sedentary
				break;
			case 1:
				bmrFactor = 1.375;// lightly
				break;
			case 2:
				bmrFactor = 1.55;// moderately
				break;
			case 3:
				bmrFactor = 1.725;// very active
				break;
			case 4:
				bmrFactor = 1.9;// extra active
				break;
		}

		totalDailyCalorieNeeds = (int) (getBmr() * bmrFactor);

		return totalDailyCalorieNeeds;
	}

	public int getIdealWeight()
	{
		return idealWeight;
	}

	public void setUser(String nameIn, int ageIn, String emailIn,
			String genderIn, int currentWeightIn, int heightIn,
			int idealWeightIn)
	{
		name = nameIn;
		age = ageIn;
		gender = genderIn;
		email = emailIn;
		currentWeight = currentWeightIn;
		height = heightIn;
		idealWeight = idealWeightIn;
	}
}
