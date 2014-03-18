package com.madmonkey.magnifitness;

import com.madmonkey.magnifitness.util.Search;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

public class ServingSizeDialogFragment extends DialogFragment
{

	TextView		foodTitle, foodType, calValue, measure_unit;
	NumberPicker	servingSize;
	View			v;
	Bundle			bundleValue;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		bundleValue = getArguments();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Select Serving Size:");
		LayoutInflater inflater = getActivity().getLayoutInflater();

		v = inflater.inflate(R.layout.select_serving_size, null);

		//Log.i("food title", bundleValue.getString("foodTitle", "not found"));
		setupAllViews();
		// setupAllViews();

		builder.setView(v);
		builder.setPositiveButton("Confirm",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						Search parentActivity = (Search) getActivity();
						parentActivity.addSelectedFood(servingSize.getValue());
					}
				});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub

					}

				});

		return builder.create();
	}

	public void setupAllViews()
	{
		final String calText = " Cal"; // add this at the end of the calValue
		foodTitle = (TextView) v.findViewById(R.id.foodTitleTV);
		foodType = (TextView) v.findViewById(R.id.foodTypeTV);
		calValue = (TextView) v.findViewById(R.id.calorieValueTV);
		measure_unit = (TextView) v.findViewById(R.id.measure_unitTV);
		servingSize = (NumberPicker) v.findViewById(R.id.servingSize);

		foodTitle.setText(bundleValue
				.getString("foodTitle", "No Food Title..."));
		foodType.setText(bundleValue.getString("foodType", "No Type..."));
		measure_unit.setText(bundleValue.getString("measure_unit", "serving."));
		calValue.setText(bundleValue.getDouble("calorie", 999) + calText);
		servingSize.setMaxValue(10);
		servingSize.setMinValue(1);
		servingSize.setValue(1);

		servingSize.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal)
			{

				calValue.setText((newVal * bundleValue
						.getDouble("calorie", 999)) + calText);

			}
		});

	}

}
