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
			case 1:
				Toast.makeText(getApplication(), "Breakfast", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(getApplication(), "Lunch", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(getApplication(), "Tea Break", Toast.LENGTH_SHORT).show();
				break;
			case 4:
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
