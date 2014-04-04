package com.madmonkey.magnifitness;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class ReminderActivity extends Activity
{
	NumberPicker	hourPicker, minutePicker, secondPicker;
	Button			breakFastBtn, lunchBtn, snackBtn, dinnerBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminder);
	}

	public void chooseMeal(View v)
	{
		setReminder();
		switch (v.getId())
		{
			case R.id.breakfastBtn:
				break;
			case R.id.lunchBtn:
				break;
			case R.id.snackBtn:
				break;
			case R.id.dinnerBtn:
				break;
		}
	}

	private void setReminder()
	{
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);

		LayoutInflater inflater = getLayoutInflater();
		View reminder = inflater.inflate(R.layout.reminder_time, null);
		helpBuilder.setView(reminder);
		
		breakFastBtn = (Button) reminder.findViewById(R.id.breakfastBtn);

		hourPicker = (NumberPicker) reminder.findViewById(R.id.hourPicker);
		minutePicker = (NumberPicker) reminder.findViewById(R.id.minutePicker);
		secondPicker = (NumberPicker) reminder.findViewById(R.id.secondPicker);

		Calendar c = Calendar.getInstance();

		hourPicker.setMaxValue(24);
		hourPicker.setMinValue(0);
		hourPicker.setValue(c.get(Calendar.HOUR_OF_DAY));

		minutePicker.setMaxValue(60);
		minutePicker.setMinValue(0);
		minutePicker.setValue(c.get(Calendar.MINUTE));

		secondPicker.setMaxValue(60);
		secondPicker.setMinValue(0);
		secondPicker.setValue(c.get(Calendar.SECOND));

		helpBuilder.setPositiveButton("Done",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which)
					{

					}
				});

		AlertDialog helpDialog = helpBuilder.create();

		helpDialog.show();
	}

}
