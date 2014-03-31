package com.madmonkey.magnifitnessClass;

import java.util.Calendar;

public abstract class Achievement {

	private String		title;
	private boolean		status;
	private Calendar	completedDate;
	private int			reward;
	private String		description;
	
	

	public Achievement(String title, int reward, String description)
		{
		this.title = title;
		this.status = false;
		this.reward = reward;
		this.description = description;
		}

	public String getTitle()
		{
		return title;
		}

	public boolean getStatus()
		{
		return status;
		}

	public Calendar getCompletedDate()
		{
		return completedDate;
		}

	public int getReward()
		{
		return reward;
		}

	public String getDescription()
		{
		return description;
		}

	public void setTitle(String title)
		{
		this.title = title;
		}

	public void setStatus(boolean status)
		{
		this.status = status;
		}

	public void setCompletedDate(Calendar completedDate)
		{
		this.completedDate = completedDate;
		}

	public void setReward(int reward)
		{
		this.reward = reward;
		}

	public void setDescription(String description)
		{
		this.description = description;
		}

}
