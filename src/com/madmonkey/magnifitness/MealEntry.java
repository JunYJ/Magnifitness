package com.madmonkey.magnifitness;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MealEntry extends Activity implements OnClickListener {

	Button	add, confirm;
	final static int BREAKFAST = 1;  
	final static int LUNCH = 2;  
	final static int SNACK = 3;  
	final static int DINNER = 4; 

	@Override
	protected void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meal_entry);
		initialization();

		Intent i = getIntent();
		int meal_type = i.getIntExtra("meal_type", 0);

		switch (meal_type)
			{
			case BREAKFAST:
				Toast.makeText(getApplication(), "Breakfast", Toast.LENGTH_SHORT).show();
				break;
			case LUNCH:
				Toast.makeText(getApplication(), "Lunch", Toast.LENGTH_SHORT).show();
				break;
			case SNACK:
				Toast.makeText(getApplication(), "Snack", Toast.LENGTH_SHORT).show();
				break;
			case DINNER:
				Toast.makeText(getApplication(), "Dinner", Toast.LENGTH_SHORT).show();
				break;
			}
		
		
		
		
		
		
		
		}

	private void initialization()
		{
		add = (Button) findViewById(R.id.add_btn);
		confirm = (Button) findViewById(R.id.confirm_btn);

		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
		{
		return super.onCreateOptionsMenu(menu);
		}

	@Override
	public void onClick(View v)
		{

		switch (v.getId())
			{

			case R.id.add_btn:

				break;

			case R.id.confirm_btn:

				break;

			}

		}

}
