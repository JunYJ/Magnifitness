package com.madmonkey.magnifitness.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.madmonkey.magnifitness.MealEntryActivity;
import com.madmonkey.magnifitness.R;
import com.madmonkey.magnifitnessClass.Food;

public class MealEntryAdapter extends ArrayAdapter<Food>
{

	private Context			context;
	private ArrayList<Food>	recordedFoodList;
	private ArrayList<Food>	tempFoodList;

	public MealEntryAdapter(Context context, ArrayList<Food> recordedFoodList)
	{
		super(context, R.layout.custom_meal_entry_list_view, recordedFoodList);
		this.context = context;
		this.recordedFoodList = recordedFoodList;
		tempFoodList = new ArrayList<Food>();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		View rowView = convertView;

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rowView = inflater.inflate(R.layout.custom_meal_entry_list_view, null);

		TextView foodTitle = (TextView) rowView.findViewById(R.id.foodTitleTV);
		TextView calValue = (TextView) rowView.findViewById(R.id.calValueTV);
		Button remove_btn = (Button) rowView.findViewById(R.id.remove_btn);

		foodTitle.setText(recordedFoodList.get(position).getTitle());

		NumberFormat formatter = new DecimalFormat("#0.00");

		double formatCalorie = recordedFoodList.get(position).getCalorie()
				* recordedFoodList.get(position).getNumOfEntry();

		calValue.setText(formatter.format(formatCalorie) + "");

		/* Log.i("ADAPTER CALORIE: ",
		 * recordedFoodList.get(position).getCalorie() + "");
		 * Log.i("ADAPTER NUM OF ENTRY: ", recordedFoodList.get(position)
		 * .getNumOfEntry() + ""); */

		remove_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Toast.makeText(context, "REMOVE", Toast.LENGTH_SHORT).show();

				tempFoodList.add(recordedFoodList.get(position));
				recordedFoodList.remove(position);

				MealEntryActivity.remove(tempFoodList);

				notifyDataSetChanged();
			}
		});

		return rowView;
	}

}
