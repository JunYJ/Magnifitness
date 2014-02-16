package com.madmonkey.magnifitness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.madmonkey.magnifitnessClass.User;

public class MealLogBook extends Fragment {

	TextView	TVcalorieValue, TVcalorieCap;
	ListView	mealList;
	Intent		nextActivity;
	User		myUser;
	Intent i;

	@Override
	public void onCreate(Bundle savedInstanceState)
		{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		nextActivity = new Intent(getActivity(), MealEntry.class);
		}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
		
		
		View rootView = inflater.inflate(R.layout.meal_logbook, container, false);
		mealList = (ListView) rootView.findViewById(R.id.mealList);
		TVcalorieValue = (TextView) rootView.findViewById(R.id.TVcalorieValue);
		TVcalorieCap = (TextView) rootView.findViewById(R.id.TVcalorieCap);
		TVcalorieCap.setText(myUser.getTotalDailyCalorieNeeds());
		
		mealList.setOnItemClickListener(new AdapterView.OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int pos, long id)
					{
					switch (pos)
						{
						case 0:

							nextActivity.putExtra("meal_type", 1);
							break;

						case 1:
							nextActivity.putExtra("meal_type", 2);
							break;

						case 2:
							nextActivity.putExtra("meal_type", 3);
							break;

						case 3:
							nextActivity.putExtra("meal_type", 4);
							break;

						}
					startActivityForResult(nextActivity, pos);

					}
			});

		return rootView;
		}

}
