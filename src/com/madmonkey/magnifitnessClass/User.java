package com.madmonkey.magnifitnessClass;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class User implements Parcelable
{
	String				name;
	int					age;
	String				email;
	String				gender;
	double				bmr;
	double				bmi;
	int					levelOfActiveness;
	int					totalDailyCalorieNeeds;
	int					currentWeight;
	int					height;
	int					idealWeight;

	static final double	NORMAL_WEIGHT	= 21.7; // (18.5 + 24.9) /2
	static final double	UNDER_WEIGHT	= 18.5;
	static final double	OVER_WEIGHT		= 24.9;
	static final double	OBESE			= 30;

	public User()
	{
		this.name = "";
		this.age = 0;
		this.email = "";
		this.gender = "";
		this.levelOfActiveness = 0;
		this.bmr = 0.0;
		this.bmi = 0.0;
		this.currentWeight = 0;
		this.height = 0;
		this.idealWeight = 0;
	}

	public User(String nameIn, int ageIn, String emailIn, String genderIn,
			int currentWeightIn, int heightIn, int activenessIn)
	{
		this.name = nameIn;
		this.age = ageIn;
		this.gender = genderIn;
		this.email = emailIn;
		this.levelOfActiveness = activenessIn;
		this.currentWeight = currentWeightIn;
		this.height = heightIn;
		this.idealWeight = getIdealWeight();
	}

	public User(Parcel in)
	{

		// All the value we need to write in

		name = in.readString();
		age = in.readInt();
		email = in.readString();
		gender = in.readString();
		bmr = in.readDouble();
		bmi = in.readDouble();
		levelOfActiveness = in.readInt();
		currentWeight = in.readInt();
		totalDailyCalorieNeeds = in.readInt();

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

		double heightInMeter = (double) getHeight();
		heightInMeter /= 100;

		double heightSquare = heightInMeter * heightInMeter;
		bmi = (double) getCurrentWeight() / heightSquare;

		return bmi;
	}

	/** Metric BMR Formula Women: BMR = 655 + ( 9.6 x weight in kilos ) + ( 1.8 x
	 * height in cm ) - ( 4.7 x age in years ) Men: BMR = 66 + ( 13.7 x weight
	 * in kilos ) + ( 5 x height in cm ) - ( 6.8 x age in years )
	 * 
	 * @return */

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
		double scalingFactor = 1;
		double userBMI = getBmi();
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

		if (userBMI < UNDER_WEIGHT)
			scalingFactor = 1.1;
		else if (userBMI > OVER_WEIGHT && userBMI < OBESE)
			scalingFactor = 0.9;
		else if (userBMI > OBESE)
			scalingFactor = 0.8;

		totalDailyCalorieNeeds = (int) ((getBmr() * bmrFactor) * scalingFactor);

		return totalDailyCalorieNeeds;
	}

	public int getIdealWeight()
	{

		double heightInMeter = (double) getHeight();
		heightInMeter /= 100;
		
		Log.i("Height in Meter", heightInMeter + " meter");

		idealWeight = (int) ((heightInMeter * heightInMeter) * NORMAL_WEIGHT);
		return idealWeight;
	}

	public void setUser(String nameIn, int ageIn, String emailIn,
			String genderIn, int currentWeightIn, int heightIn, int activenessIn)
	{
		name = nameIn;
		age = ageIn;
		levelOfActiveness = activenessIn;
		gender = genderIn;
		email = emailIn;
		currentWeight = currentWeightIn;
		height = heightIn;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags)
	{
		out.writeString(name);
		out.writeInt(age);
		out.writeString(email);
		out.writeString(gender);
		out.writeDouble(bmr);
		out.writeDouble(bmi);
		out.writeInt(levelOfActiveness);
		out.writeInt(currentWeight);
		out.writeInt(totalDailyCalorieNeeds);

	}

	public static final Parcelable.Creator<User>	CREATOR	= new Parcelable.Creator<User>() {
																public User createFromParcel(
																		Parcel in)
																{
																	return new User(
																			in);
																}

																public User[] newArray(
																		int size)
																{
																	return new User[size];
																}
															};

}
