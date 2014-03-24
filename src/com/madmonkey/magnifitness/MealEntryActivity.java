package com.madmonkey.magnifitness;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.madmonkey.magnifitness.util.DatabaseHandler;
import com.madmonkey.magnifitness.util.MealEntryAdapter;
import com.madmonkey.magnifitness.util.Search;
import com.madmonkey.magnifitnessClass.Food;
import com.madmonkey.magnifitnessClass.MealEntry;

public class MealEntryActivity extends Activity implements OnClickListener
{

	Button					add, confirm;
	TextView				mealEntryInfoTV;
	// String[] foodList;
	DatabaseHandler			dbHandler;
	ListView				foodListView;
	ArrayList<String>		selectedFood;
	ArrayList<Food>			selectedFoodObjList;
	Intent					returnIntent;

	double					totalCalorie;
	MealEntry				mealEntry;

	MealEntry				dbMealEntry;
	ArrayList<MealEntry>	list_of_meal_entry;
	boolean					newFoodAdded;

	final static int		BREAKFAST	= 1;
	final static int		LUNCH		= 2;
	final static int		SNACK		= 3;
	final static int		DINNER		= 4;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meal_entry);
		initialization();

		Intent i = getIntent();
		int meal_type = i.getIntExtra("meal_type", 0);

		Calendar c = Calendar.getInstance();
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		String date = "" + c.get(Calendar.DAY_OF_MONTH) + "th "
				+ months[(c.get(Calendar.MONTH))] + " " + c.get(Calendar.YEAR);

		dbMealEntry = null;

		list_of_meal_entry = (ArrayList<MealEntry>) dbHandler.getAllMealEntry();
		Log.i("list of meal entry size", list_of_meal_entry.size() + "");
		
		if (list_of_meal_entry.size() != 0)
		{
			Log.i("list of meal entry size", list_of_meal_entry.size() + "");
			dbMealEntry = dbHandler.getMealEntry("Breakfast", date);
			selectedFoodObjList = dbMealEntry.getFoodList();
			foodListView.setAdapter(new MealEntryAdapter(this,
					selectedFoodObjList));
		}

		switch (meal_type)
		{
			case BREAKFAST:
				Toast.makeText(getApplication(), "Breakfast",
						Toast.LENGTH_SHORT).show();
				mealEntry.setMealType("Breakfast");
				mealEntryInfoTV.setText("Breakfast, on "
						+ mealEntry.getDateStr());
				break;
			case LUNCH:
				Toast.makeText(getApplication(), "Lunch", Toast.LENGTH_SHORT)
						.show();
				mealEntry.setMealType("Lunch");
				mealEntryInfoTV.setText("Lunch, on " + mealEntry.getDateStr());
				break;
			case SNACK:
				Toast.makeText(getApplication(), "Snack", Toast.LENGTH_SHORT)
						.show();
				mealEntry.setMealType("Snack");
				mealEntryInfoTV.setText("Snack, on " + mealEntry.getDateStr());
				break;
			case DINNER:
				Toast.makeText(getApplication(), "Dinner", Toast.LENGTH_SHORT)
						.show();
				mealEntry.setMealType("Dinner");
				mealEntryInfoTV.setText("Dinner, on " + mealEntry.getDateStr());
				break;
		}

	}

	private void initialization()
	{
		dbHandler = new DatabaseHandler(this);

		add = (Button) findViewById(R.id.add_btn);
		confirm = (Button) findViewById(R.id.confirm_btn);
		mealEntryInfoTV = (TextView) findViewById(R.id.mealEntryInfoTV);

		add.setOnClickListener(this);
		confirm.setOnClickListener(this);

		foodListView = (ListView) findViewById(R.id.food_records);
		selectedFood = new ArrayList<String>();
		selectedFoodObjList = new ArrayList<Food>();
		totalCalorie = 0.0;

		returnIntent = new Intent();

		mealEntry = new MealEntry();
		
		newFoodAdded = false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onClick(View v)
	{
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

				// Log.i("TOTAL CALORIE: ", totalCalorie+ "");
				if(dbMealEntry != null)
					returnIntent.putExtra("totalCalorie", totalCalorie + dbMealEntry.getTotalCalorie());
				else
					returnIntent.putExtra("totalCalorie", totalCalorie);
				
				
				returnIntent.putExtra("newFoodAdded", newFoodAdded);
				setResult(RESULT_OK, returnIntent);

				if(dbMealEntry == null)
					dbHandler.addMealEntry(mealEntry);
				else if(dbMealEntry != null && newFoodAdded == true)
				{
					dbMealEntry.setTotalCalorie(dbMealEntry.getTotalCalorie() + mealEntry.getTotalCalorie());
					dbHandler.updateMealEntry(dbMealEntry);
				}
				// dbHandler.getMealEntry("Breakfast", "19th March 2014");

				this.finish();
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

				String titleIn = data.getStringExtra("foodTitle");
				String measurementUnitIn = data.getStringExtra("measure_unit");
				Double calorieIn = data.getDoubleExtra("calorie", 999);
				String typeIn = data.getStringExtra("foodType");

				Food f = new Food(titleIn, measurementUnitIn, calorieIn, typeIn);

				boolean duplicate = false;
				for (int i = 0; i < selectedFoodObjList.size(); i++)
				{
					if (selectedFoodObjList.get(i).getTitle()
							.equals(f.getTitle()))
					{
						duplicate = true;
						break;
					}
				}

				if (duplicate == false)
					selectedFoodObjList.add(f);
				else
				{
					for (int i = 0; i < selectedFoodObjList.size(); i++)
					{
						if (selectedFoodObjList.get(i).getTitle()
								.equals(f.getTitle()))
						{
							selectedFoodObjList.get(i).setCalorie(
									selectedFoodObjList.get(i).getCalorie()
											+ f.getCalorie());
							break;
						}
					}
				}

				foodListView.setAdapter(new MealEntryAdapter(this,
						selectedFoodObjList));

				double calorie = data.getDoubleExtra("calorie", 0.0);
				// Log.i("Meal Entry (CALORIE): ", calorie + "");
				totalCalorie += calorie;

				mealEntry.setTotalCalorie(totalCalorie);
				mealEntry.setFoodList(selectedFoodObjList);
				// Log.i("Meal Entry (DATE): ", me.getDateStr());

				/* ArrayAdapter<String> fa = new ArrayAdapter<String>(
				 * getBaseContext(), android.R.layout.simple_list_item_1,
				 * selectedFood); */
				// foodListView.setAdapter(fa);

				/* Log.i("Meal Entry(Serving Size): ",
				 * data.getIntExtra("servingSize", 0) + ""); */
				
				newFoodAdded = true;
			}
		}
	}

}
