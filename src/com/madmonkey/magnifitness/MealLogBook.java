package com.madmonkey.magnifitness;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MealLogBook extends Fragment {

	ListView	mealList;

	@Override
	public void onCreate(Bundle savedInstanceState)
		{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
		View rootView = inflater.inflate(R.layout.meal_logbook, container, false);
		mealList = (ListView) rootView.findViewById(R.id.mealList);

		mealList.setOnItemClickListener(new AdapterView.OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int pos, long id)
					{
					switch (pos)
						{
						case 0:
							Toast.makeText(parent.getContext(), "Breakfast selected", Toast.LENGTH_LONG).show();
							break;

						case 1:
							Toast.makeText(parent.getContext(), "Lunch Selected", Toast.LENGTH_LONG).show();
							break;

						case 2:
							Toast.makeText(parent.getContext(), "Tea-break Selected", Toast.LENGTH_LONG).show();
							break;

						case 3:
							Toast.makeText(parent.getContext(), "Dinner Selected", Toast.LENGTH_LONG).show();
							break;
						}

					}
			});

		return rootView;
		}

}
