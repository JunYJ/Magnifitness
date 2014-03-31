package com.madmonkey.magnifitness.util;

public class PedometerCounter 
{
	int steps;
	boolean status;
	
	public PedometerCounter()
	{
		steps = 0;
		status = false;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
