package com.madmonkey.magnifitness;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MealEntry extends Activity{

	TextView TVcalorieValue;
	ImageView idealMealPlanIV, myMealPlanIV;
	
	Button add, cancel, confirm;


	@Override
	protected void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		
		
		
		
		initialization();
		
		}

	private void initialization()
		{
		TVcalorieValue = (TextView) findViewById(R.id.TVcalorieValue);
		idealMealPlanIV = (ImageView) findViewById(R.id.idealMealPlanIV);
		myMealPlanIV = (ImageView) findViewById(R.id.myMealPlanIV);
		add = (Button) findViewById(R.id.add_btn);
		confirm = (Button) findViewById(R.id.confirm_btn);
		cancel = (Button) findViewById(R.id.cancel_btn);
		
		
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
		{
		return super.onCreateOptionsMenu(menu);
		}





}
