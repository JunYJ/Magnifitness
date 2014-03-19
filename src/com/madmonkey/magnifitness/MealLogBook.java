package com.madmonkey.magnifitness;

import java.text.DecimalFormat;
import java.text.NumberFormat;

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

import com.madmonkey.magnifitnessClass.User;

public class MealLogBook extends Fragment {

	TextView			TVcalorieValue, TVcalorieCap;
	ListView			mealList;
	ProgressBar			calorieProgressBar;
	Intent				nextActivity;
	
	User				user;
	
	int calorieCap;
	double currentCalorie;
	
	final static int BREAKFAST = 1;  
	final static int LUNCH = 2;  
	final static int SNACK = 3;  
	final static int DINNER = 4;  
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
		}

	@Override
	public void onAttach(Activity activity)
		{
		super.onAttach(activity);

		}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{

		View rootView = inflater.inflate(R.layout.meal_logbook, container, false);
		
		TVcalorieValue = (TextView) rootView.findViewById(R.id.TVcalorieValue);
		TVcalorieCap = (TextView) rootView.findViewById(R.id.TVcalorieCap);
		//TVcalorieCap.setText(user.getTotalDailyCalorieNeeds() + "");
		TVcalorieCap.setText(calorieCap + "");
		
		calorieProgressBar = (ProgressBar) rootView.findViewById(R.id.calorieBar);
		calorieProgressBar.setMax(Integer.parseInt(TVcalorieCap.getText().toString()));
		calorieProgressBar.setProgress(Integer.parseInt(TVcalorieValue.getText().toString()));
		
		mealList = (ListView) rootView.findViewById(R.id.mealList);
		mealList.setOnItemClickListener(new AdapterView.OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int pos, long id)
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
		int age, weight, height, idealWeight, lvOfActiveness;

		if (userSP.getBoolean("userCreated", false))
			{
			username = userSP.getString("name", "unknown");
			email = userSP.getString("email", "unknown");
			gender = userSP.getString("gender", "unknown");
			age = userSP.getInt("age", 99);
			lvOfActiveness = userSP.getInt("lvOfActiveness", 0);
			weight = userSP.getInt("weight", 1);
			height = userSP.getInt("height", 1);
			idealWeight = userSP.getInt("idealWeight", 2);

			user = new User(username, age, email, gender, weight, height, lvOfActiveness);

			}

		}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == 0 || requestCode == 1 || requestCode == 2 || requestCode == 3)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				currentCalorie = Double.parseDouble(TVcalorieValue.getText().toString());
				currentCalorie += data.getDoubleExtra("totalCalorie", 0.0);
				NumberFormat formatter = new DecimalFormat("#0.00"); 
				//Log.i("CURRENT CALORIE", currentCalorie + "");
				double formatCalorie = currentCalorie;
				TVcalorieValue.setText(formatter.format(formatCalorie) + "");
				calorieProgressBar.setProgress((int) currentCalorie);
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
