package com.madmonkey.magnifitness.util;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.madmonkey.magnifitness.R;
import com.madmonkey.magnifitnessClass.Food;

public class MealEntryAdapter extends ArrayAdapter<Food>{

private final Context context;
private final ArrayList<Food> recordedFoodList;


public MealEntryAdapter(Context context, ArrayList<Food> recordedFoodList) {
	super(context, R.layout.custom_meal_entry_list_view, recordedFoodList);
	this.context = context;
	this.recordedFoodList = recordedFoodList;

}


@Override
public View getView(int position, View convertView, ViewGroup parent)
	{
	View rowView = convertView;
	
	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	rowView = inflater.inflate(R.layout.custom_meal_entry_list_view, null);
	
	TextView foodTitle = (TextView) rowView.findViewById(R.id.foodTitleTV);
	TextView calValue = (TextView) rowView.findViewById(R.id.calValueTV);
	Button remove_btn = (Button) rowView.findViewById(R.id.remove_btn);
	
	

	foodTitle.setText(recordedFoodList.get(position).getTitle());
	calValue.setText(recordedFoodList.get(position).getCalorie() + "");
	
	
	
	
	return rowView;
	}




}
