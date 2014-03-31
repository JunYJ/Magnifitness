package com.madmonkey.magnifitnessClass;

public class PedoAchievement extends Achievement {

	private int	currentStepCount;
	private int	targetStepCount;

	public PedoAchievement(String title, int reward, String description, int targetStepCount)
		{
		super(title, reward, description);
		this.currentStepCount = 0;
		this.targetStepCount = targetStepCount;
		}

	public int getCurrentStepCount()
		{
		return currentStepCount;
		}

	public int getTargetStepCount()
		{
		return targetStepCount;
		}

	public void setCurrentStepCount(int currentStepCount)
		{
		this.currentStepCount = currentStepCount;
		}

	public void setTargetStepCount(int targetStepCount)
		{
		this.targetStepCount = targetStepCount;
		}

}
