package com.madmonkey.magnifitnessClass;

public class CalorieAchievement extends Achievement {

	private double	accumCalorie;
	private double	targetCalorie;

	public CalorieAchievement(String title, int reward, String description, double targetCalorie)
		{
		super(title, reward, description);
		this.accumCalorie = 0.0;
		this.targetCalorie = targetCalorie;
		}

	public double getAccumCalorie()
		{
		return accumCalorie;
		}

	public double getTargetCalorie()
		{
		return targetCalorie;
		}

	public void setAccumCalorie(double accumCalorie)
		{
		this.accumCalorie = accumCalorie;
		}

	public void setTargetCalorie(double targetCalorie)
		{
		this.targetCalorie = targetCalorie;
		}

}
