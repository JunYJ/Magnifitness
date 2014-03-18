package com.madmonkey.magnifitness;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.madmonkey.magnifitness.util.DatabaseHandler;
import com.madmonkey.magnifitness.util.Search;

public class MealEntry extends Activity implements OnClickListener
{

	Button				add, confirm;
	// String[] foodList;
	DatabaseHandler		dbHandler;
	ListView			foodListView;
	ArrayList<String>	selectedFood;
	Intent				returnIntent;
	double totalCalorie;

	final static int	BREAKFAST	= 1;
	final static int	LUNCH		= 2;
	final static int	SNACK		= 3;
	final static int	DINNER		= 4;

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
				Toast.makeText(getApplication(), "Breakfast",
						Toast.LENGTH_SHORT).show();
				break;
			case LUNCH:
				Toast.makeText(getApplication(), "Lunch", Toast.LENGTH_SHORT)
						.show();
				break;
			case SNACK:
				Toast.makeText(getApplication(), "Snack", Toast.LENGTH_SHORT)
						.show();
				break;
			case DINNER:
				Toast.makeText(getApplication(), "Dinner", Toast.LENGTH_SHORT)
						.show();
				break;
		}

	}

	private void initialization()
	{
		add = (Button) findViewById(R.id.add_btn);
		confirm = (Button) findViewById(R.id.confirm_btn);

		add.setOnClickListener(this);
		confirm.setOnClickListener(this);

		foodListView = (ListView) findViewById(R.id.food_records);
		selectedFood = new ArrayList<String>();
		totalCalorie = 0.0;
		
		returnIntent = new Intent();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onClick(View v)
	{
		dbHandler = new DatabaseHandler(this);
		/* ArrayList<Food> list = (ArrayList<Food>) dbHandler.getAllFood(); for
		 * (int i = 0; i < list.size(); i++) { Log.i("DATABASE SIZE TEST: ",
		 * list.get(i).toString()); } */

		switch (v.getId())
		{

			case R.id.add_btn:
				Intent i = new Intent(this, Search.class);
				startActivityForResult(i, 1);

				break;

			case R.id.confirm_btn:
				Log.i("confirm_button", "Confirm button clicked");

				Log.i("TOTAL CALORIE: ", totalCalorie+ "");
				returnIntent.putExtra("totalCalorie", totalCalorie);
				setResult(RESULT_OK, returnIntent);
				finish();
				break;

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1)
		{
			if (resultCode == RESULT_OK)
			{
				selectedFood.add(dbHandler.getFood(
						data.getStringExtra("foodTitle")).getTitle());

				double calorie = data.getDoubleExtra("calorie", 0.0);
				Log.i("Meal Entry (CALORIE): ", calorie + "");
				totalCalorie += calorie;

				ArrayAdapter<String> fa = new ArrayAdapter<String>(
						getBaseContext(), android.R.layout.simple_list_item_1,
						selectedFood);
				foodListView.setAdapter(fa);

				Log.i("Meal Entry(Serving Size): ",
						data.getIntExtra("servingSize", 0) + "");
			}
		}
	}

}
