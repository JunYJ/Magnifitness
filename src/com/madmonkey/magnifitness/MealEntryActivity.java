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

	DatabaseHandler			dbHandler;
	ListView				foodListView;
	ArrayList<Food>			foodList;
	static ArrayList<Food>	foodToBeRemovedList;
	Intent					returnIntent;

	double					totalCalorie;
	static MealEntry		mealEntry;

	static MealEntry		recordedMealEntry;
	ArrayList<MealEntry>	mealEntryList;
	boolean					newFoodAdded;
	static boolean			foodRemoved;

	final static int		BREAKFAST	= 1;
	final static int		LUNCH		= 2;
	final static int		SNACK		= 3;
	final static int		DINNER		= 4;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meal_entry);
		
		Intent i = getIntent();
		int meal_type = i.getIntExtra("meal_type", 0);
		
		initialization(meal_type);
		checkMealEntryExistence(meal_type);
	}

	private void initialization(int meal_type)
	{
		// DB
		dbHandler = new DatabaseHandler(this);

		// View
		add = (Button) findViewById(R.id.add_btn);
		confirm = (Button) findViewById(R.id.confirm_btn);

		mealEntryInfoTV = (TextView) findViewById(R.id.mealEntryInfoTV);

		foodListView = (ListView) findViewById(R.id.food_records);

		// Intent for passing data to calling activity
		returnIntent = new Intent();

		// Flag for specific meal entry of the day
		recordedMealEntry = null;

		// food list of existing meal entry
		foodList = new ArrayList<Food>();

		// Total calorie of temp Meal Entry
		// Either a new Meal Entry OR
		// accumulated into existing Meal Entry's total calorie
		totalCalorie = 0.0;

		// temp Meal Entry
		mealEntry = new MealEntry();

		//Flag for determine whether a new Food is added into Meal Entry
		//Meal Entry will not be created or update if no new Food is added into foodList
		newFoodAdded = false;
		
		foodRemoved = false;
		
		// assign data to temp mealEntry
		// for adding into database
		// or updating existing meal entry
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

	private void checkMealEntryExistence(int meal_type)
	{
		// Get current date
		Calendar c = Calendar.getInstance();
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		String date = "" + c.get(Calendar.DAY_OF_MONTH) + "th "
				+ months[(c.get(Calendar.MONTH))] + " " + c.get(Calendar.YEAR);

		mealEntryList = (ArrayList<MealEntry>) dbHandler
				.getTodayMealEntry(date);
		// Log.i("Number of meal entry in DB: ", mealEntryList.size() + "");
		ArrayList<MealEntry> test = (ArrayList<MealEntry>) dbHandler.getAllMealEntry();
		Log.i("Number of recorded meal entry: ", test.size() + "");

		// if there's is meal entry recorded for today
		if (mealEntryList.size() != 0)
		{
			Log.i("Number of meal entry of today: ", mealEntryList.size() + "");

			// find specific meal entry of the day
			if (meal_type == 1)
				recordedMealEntry = dbHandler.getMealEntry("Breakfast", date);
			else if (meal_type == 2)
				recordedMealEntry = dbHandler.getMealEntry("Lunch", date);
			else if (meal_type == 3)
				recordedMealEntry = dbHandler.getMealEntry("Snack", date);
			else if (meal_type == 4)
				recordedMealEntry = dbHandler.getMealEntry("Dinner", date);

			// if is found
			if (recordedMealEntry != null)
			{
				// load the food list of found Meal Entry for display
				foodList = recordedMealEntry.getFoodList();
				foodListView.setAdapter(new MealEntryAdapter(this, foodList));
				//Log.i("TEST", foodList.get(0).getNumOfEntry() + "");
				//Log.i("TEST", foodList.get(1).getNumOfEntry() + "");
			}

		}
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
			//Search food
			case R.id.add_btn:
				Intent i = new Intent(this, Search.class);
				startActivityForResult(i, 1);

				break;
			//Add/Update Meal Entry
			case R.id.confirm_btn:
				Log.i("confirm_button", "Confirm button clicked");

				// Log.i("TOTAL CALORIE: ", totalCalorie+ "");
				
				//sum of total calorie of exist Meal Entry & temp Meal Entry
				if (recordedMealEntry != null)
					returnIntent.putExtra("totalCalorie", totalCalorie
							+ recordedMealEntry.getTotalCalorie());
				//if specific Meal Entry of the day is not exist
				//just get the total calorie of the temp Meal Entry
				else
					returnIntent.putExtra("totalCalorie", totalCalorie);

				//Flag for notifying calling activity
				returnIntent.putExtra("newFoodAdded", newFoodAdded);
				setResult(RESULT_OK, returnIntent);

				//Added Meal Entry ONLY if foodList is not EMPTY & specific Meal Entry is not exist
				if (recordedMealEntry == null && newFoodAdded == true)
				{
					if(mealEntry.getFoodList().size() > 0)
						dbHandler.addMealEntry(mealEntry);
				}
				//Update existing Meal Entry if it is found & new Food is added
				else if (recordedMealEntry != null && newFoodAdded == true)
				{
					ArrayList<Food> testing2 = recordedMealEntry.getFoodList();
					
					Log.i("TEST4", testing2.size() + "");
					
					recordedMealEntry.setTotalCalorie(recordedMealEntry
							.getTotalCalorie() + mealEntry.getTotalCalorie());
					dbHandler.updateMealEntry(recordedMealEntry);
				}
				
				if(recordedMealEntry != null && foodRemoved == true)
				{
					dbHandler.removeFoodFromMealEntry(recordedMealEntry, foodToBeRemovedList);
					returnIntent.putExtra("foodRemoved", foodRemoved);
					setResult(RESULT_OK, returnIntent);
					foodRemoved = false;
				}
				else
				
				//Reset flag
				newFoodAdded = false;
				
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
				String titleIn = data.getStringExtra("foodTitle");
				String measurementUnitIn = data.getStringExtra("measure_unit");
				Double calorieIn = data.getDoubleExtra("calorie", 999);
				String typeIn = data.getStringExtra("foodType");
				int numOfEntryIn = data.getIntExtra("servingSize", 0);
				Log.i("SERVING SIZE: ", numOfEntryIn + "");

				// Create a Food object
				Food food = new Food(titleIn, measurementUnitIn, calorieIn,
						typeIn);
				food.setNumOfEntry(numOfEntryIn);

				// Flag for duplication Food in the
				boolean duplicate = false;
				for (int i = 0; i < foodList.size(); i++)
				{
					if (foodList.get(i).getTitle().equals(food.getTitle()))
					{
						duplicate = true;
						break;
					}
				}

				//if not duplication of Food
				if (duplicate == false)
				{					
					foodList.add(food);
				}
					
				//Duplication detected
				//Update total calorie ONLY
				else
				{
					for (int i = 0; i < foodList.size(); i++)
					{
						if (foodList.get(i).getTitle().equals(food.getTitle()))
						{
							/*foodList.get(i).setCalorie(
									foodList.get(i).getCalorie()
											+ food.getCalorie());*/
							
							Log.i("NUM OF ENTRY BEFORE: ", foodList.get(i).getNumOfEntry() + "");
							foodList.get(i).setNumOfEntry(foodList.get(i).getNumOfEntry() + food.getNumOfEntry());
							Log.i("NUM OF ENTRY AFTER: ", foodList.get(i).getNumOfEntry() + "");
							break;
						}
					}
				}
				
				foodListView.setAdapter(new MealEntryAdapter(this, foodList));

				//temp Meal Entry
				double calorie = data.getDoubleExtra("calorie", 0.0);
				totalCalorie += calorie;

				mealEntry.setTotalCalorie(totalCalorie);
				mealEntry.setFoodList(foodList);
				
				// Log.i("Meal Entry (DATE): ", me.getDateStr());

				/* ArrayAdapter<String> fa = new ArrayAdapter<String>(
				 * getBaseContext(), android.R.layout.simple_list_item_1,
				 * selectedFood); */
				// foodListView.setAdapter(fa);

				/* Log.i("Meal Entry(Serving Size): ",
				 * data.getIntExtra("servingSize", 0) + ""); */

				//Flag for notifying new Food is added
				newFoodAdded = true;
			}
		}
	}

	public static void remove(ArrayList<Food> fl)
	{
		foodToBeRemovedList = new ArrayList<Food>();
		foodToBeRemovedList = fl;

		/*for(int i = 0; i < foodToBeRemovedList.size(); i++)
		{
			Log.i("TO BE REMOVED: ", foodToBeRemovedList.get(i).getTitle());
		}*/
		foodRemoved = true;	
	}

}
