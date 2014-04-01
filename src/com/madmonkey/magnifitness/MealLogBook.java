package com.madmonkey.magnifitness;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.madmonkey.magnifitness.util.DatabaseHandler;
import com.madmonkey.magnifitnessClass.MealEntry;
import com.madmonkey.magnifitnessClass.User;

public class MealLogBook extends Fragment
{

	TextView			TVcalorieValue, TVcalorieCap;
	ListView			mealTypeList;
	ProgressBar			calorieProgressBar;
	Intent				nextActivity;
	DatabaseHandler		db;

	User				user;

	int					calorieCap;
	double				currentCalorie;

	final static int	BREAKFAST	= 1;
	final static int	LUNCH		= 2;
	final static int	SNACK		= 3;
	final static int	DINNER		= 4;
	SharedPreferences	userSP;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		nextActivity = new Intent(getActivity(), MealEntryActivity.class);
		currentCalorie = 0.0;

		Bundle bundle = this.getArguments();
		calorieCap = bundle.getInt("calorieCap");
		currentCalorie = bundle.getDouble("todayCalorie");

		db = new DatabaseHandler(getActivity());
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.meal_logbook, container,
				false);

		// Calorie consumed
		TVcalorieValue = (TextView) rootView.findViewById(R.id.TVcalorieValue);
		NumberFormat formatter = new DecimalFormat("#0.00");

		double formatCalorie = currentCalorie;

		// display updated calorie
		TVcalorieValue
				.setText(formatter.format(formatCalorie) + "");

		// Calories required/limit
		TVcalorieCap = (TextView) rootView.findViewById(R.id.TVcalorieCap);
		TVcalorieCap.setText(calorieCap + "");

		// Progress bar (sync with calorie consumed)
		calorieProgressBar = (ProgressBar) rootView
				.findViewById(R.id.calorieBar);
		calorieProgressBar.setMax(Integer.parseInt(TVcalorieCap.getText()
				.toString()));
		calorieProgressBar.setProgress((int) Double.parseDouble(TVcalorieValue
				.getText().toString()));

		// List of Meal type
		mealTypeList = (ListView) rootView.findViewById(R.id.mealList);
		mealTypeList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int pos, long id)
					{
						switch (pos)
						{
							case 0:
								nextActivity.putExtra("meal_type", BREAKFAST);
								break;

							case 1:
								nextActivity.putExtra("meal_type", LUNCH);
								break;

							case 2:
								nextActivity.putExtra("meal_type", SNACK);
								break;

							case 3:
								nextActivity.putExtra("meal_type", DINNER);
								break;

						}

						startActivityForResult(nextActivity, pos);
					}
				});

		return rootView;
	}

	public void loadFromPref()
	{
		userSP = getActivity().getSharedPreferences(FacebookLogin.filename, 0);
		String username, email, gender;
		int age, weight, height, lvOfActiveness;

		if (userSP.getBoolean("userCreated", false))
		{
			username = userSP.getString("name", "unknown");
			email = userSP.getString("email", "unknown");
			gender = userSP.getString("gender", "unknown");
			age = userSP.getInt("age", 99);
			lvOfActiveness = userSP.getInt("lvOfActiveness", 0);
			weight = userSP.getInt("weight", 1);
			height = userSP.getInt("height", 1);
			// idealWeight = userSP.getInt("idealWeight", 2);

			user = new User(username, age, email, gender, weight, height,
					lvOfActiveness);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 0 || requestCode == 1 || requestCode == 2
				|| requestCode == 3)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				// if new food is added into one of the meal entry
				// update calorie consumed of the day
				if (data.getBooleanExtra("newFoodAdded", false) == true || data.getBooleanExtra("foodRemoved", false) == true)
				{
					Calendar c = Calendar.getInstance();
					DateFormatSymbols dfs = new DateFormatSymbols();
					String[] months = dfs.getMonths();
					String date = "";
					if(c.get(Calendar.DAY_OF_MONTH) == 1 || c.get(Calendar.DAY_OF_MONTH) == 11 
							|| c.get(Calendar.DAY_OF_MONTH) == 21 || c.get(Calendar.DAY_OF_MONTH) == 31)
					{
						date = "" + c.get(Calendar.DAY_OF_MONTH) + "st "
								+ months[(c.get(Calendar.MONTH))] + " " + c.get(Calendar.YEAR);
					}
					else if(c.get(Calendar.DAY_OF_MONTH) == 2 || c.get(Calendar.DAY_OF_MONTH) == 12 || c.get(Calendar.DAY_OF_MONTH) == 22)
					{
						date = "" + c.get(Calendar.DAY_OF_MONTH) + "nd "
								+ months[(c.get(Calendar.MONTH))] + " " + c.get(Calendar.YEAR);
					}
					else if(c.get(Calendar.DAY_OF_MONTH) == 3 || c.get(Calendar.DAY_OF_MONTH) == 13 || c.get(Calendar.DAY_OF_MONTH) == 23)
					{
						date = "" + c.get(Calendar.DAY_OF_MONTH) + "rd "
								+ months[(c.get(Calendar.MONTH))] + " " + c.get(Calendar.YEAR);
					}
					else
					{
						date = "" + c.get(Calendar.DAY_OF_MONTH) + "th "
								+ months[(c.get(Calendar.MONTH))] + " " + c.get(Calendar.YEAR);
					}

					ArrayList<MealEntry> mealEntryList = (ArrayList<MealEntry>) db
							.getTodayMealEntry(date);

					// get calorie of view
					/*currentCalorie = Double.parseDouble(TVcalorieValue
							.getText().toString());*/

					// (update/accumulate) current calorie
					//currentCalorie += data.getDoubleExtra("totalCalorie", 0.0);

					currentCalorie = 0.0;
					for (int i = 0; i < mealEntryList.size(); i++)
					{
						currentCalorie += mealEntryList.get(i)
								.getTotalCalorie();
					}

					// format into two decimal point
					NumberFormat formatter = new DecimalFormat("#0.00");

					double formatCalorie = currentCalorie;

					// display updated calorie
					TVcalorieValue
							.setText(formatter.format(formatCalorie) + "");
					// update progress bar
					calorieProgressBar.setProgress((int) currentCalorie);
				}
			}
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
		loadFromPref();
		TVcalorieCap.setText(user.getTotalDailyCalorieNeeds() + "");
	}

}
