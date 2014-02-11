package com.madmonkey.magnifitness;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MealEntry extends Activity {

	Button		add, cancel, confirm;

	@Override
	protected void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meal_entry);
		initialization();

		Intent i = getIntent();

		if (i.getIntExtra("meal_type", 0) == 1)
			{
				
			
			
			
			
			}

		}

	private void initialization()
		{
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
