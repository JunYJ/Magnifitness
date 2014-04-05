package com.madmonkey.magnifitness;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.madmonkey.magnifitness.util.AlarmReceiver;

public class ReminderActivity extends Activity
{
	NumberPicker	hourPicker, minutePicker, secondPicker;
	Button			breakFastBtn, lunchBtn, snackBtn, dinnerBtn;
	Button			cancelBreakFastBtn, cancelLunchBtn, cancelSnackBtn,
			cancelDinnerBtn;

	private int		BREAKFAST	= 1;
	private int		LUNCH		= 2;
	private int		SNACK		= 3;
	private int		DINNER		= 4;

	String			timeStr;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminder);

		breakFastBtn = (Button) findViewById(R.id.breakfastBtn);
		lunchBtn = (Button) findViewById(R.id.lunchBtn);
		snackBtn = (Button) findViewById(R.id.snackBtn);
		dinnerBtn = (Button) findViewById(R.id.dinnerBtn);

		cancelBreakFastBtn = (Button) findViewById(R.id.cancelBreakFastBtn);
		cancelLunchBtn = (Button) findViewById(R.id.cancelLunchBtn);
		cancelSnackBtn = (Button) findViewById(R.id.cancelSnackBtn);
		cancelDinnerBtn = (Button) findViewById(R.id.cancelDinnerBtn);
		timeStr = "";
	}

	public void chooseMeal(View v)
	{
		switch (v.getId())
		{
			case R.id.breakfastBtn:
				setReminder(BREAKFAST);
				break;
			case R.id.lunchBtn:
				setReminder(LUNCH);
				break;
			case R.id.snackBtn:
				setReminder(SNACK);
				break;
			case R.id.dinnerBtn:
				setReminder(DINNER);
				break;
		}
	}

	private void setReminder(final int meal)
	{
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);

		LayoutInflater inflater = getLayoutInflater();
		View reminder = inflater.inflate(R.layout.reminder_time, null);
		helpBuilder.setView(reminder);

		hourPicker = (NumberPicker) reminder.findViewById(R.id.hourPicker);
		minutePicker = (NumberPicker) reminder.findViewById(R.id.minutePicker);
		secondPicker = (NumberPicker) reminder.findViewById(R.id.secondPicker);

		Calendar c = Calendar.getInstance();

		hourPicker.setMaxValue(23);
		hourPicker.setMinValue(0);
		hourPicker.setValue(c.get(Calendar.HOUR_OF_DAY));

		minutePicker.setMaxValue(59);
		minutePicker.setMinValue(0);
		minutePicker.setValue(c.get(Calendar.MINUTE));

		secondPicker.setMaxValue(59);
		secondPicker.setMinValue(0);
		secondPicker.setValue(c.get(Calendar.SECOND));

		helpBuilder.setPositiveButton("Done",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which)
					{
						int hour = hourPicker.getValue();
						int minute = minutePicker.getValue();
						int second = secondPicker.getValue();
						// int milliSecond = (hour * 3600 + minute * 60 +
						// second) * 1000;

						GregorianCalendar c = (GregorianCalendar) GregorianCalendar
								.getInstance();
						c.set(GregorianCalendar.HOUR_OF_DAY, hour);
						c.set(GregorianCalendar.MINUTE, minute);
						c.set(GregorianCalendar.SECOND, second);

						Long time = c.getTimeInMillis();

						Intent intentAlarm = new Intent(getBaseContext(),
								AlarmReceiver.class);

						// create the object
						AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

						// set the alarm for particular time
						alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
								time, AlarmManager.INTERVAL_DAY,
								PendingIntent.getBroadcast(getBaseContext(),
										meal, intentAlarm,
										PendingIntent.FLAG_UPDATE_CURRENT));
						Toast.makeText(getBaseContext(),
								"Alarm Scheduled for " + time,
								Toast.LENGTH_LONG).show();
						if (hour < 10)
							timeStr = timeStr + "0" + hour + ":";
						else
							timeStr = timeStr + hour + ":";
						if (minute < 10)
							timeStr = timeStr + "0" + minute + ":";
						else
							timeStr = timeStr + minute + ":";
						if (second < 10)
							timeStr = timeStr + "0" + second;
						else
							timeStr = timeStr + second;

						// timeStr = hour + ":" + minute + ":" + second;

						if (meal == 1)
							breakFastBtn.setText("Breakfast set at " + timeStr);
						else if (meal == 2)
							lunchBtn.setText("Lunch set at " + timeStr);
						else if (meal == 3)
							snackBtn.setText("Snack set at " + timeStr);
						else if (meal == 4)
							dinnerBtn.setText("Dinner set at " + timeStr);

						timeStr = "";
					}
				});

		AlertDialog helpDialog = helpBuilder.create();

		helpDialog.show();
	}

	public void cancelReminder(View v)
	{
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intentAlarm = new Intent(getBaseContext(), AlarmReceiver.class);

		switch (v.getId())
		{
			case R.id.cancelBreakFastBtn:

				alarmManager.cancel(PendingIntent.getBroadcast(
						getBaseContext(), BREAKFAST, intentAlarm,
						PendingIntent.FLAG_UPDATE_CURRENT));
				breakFastBtn.setText("Breakfast not set");
				break;
			case R.id.cancelLunchBtn:
				alarmManager.cancel(PendingIntent.getBroadcast(
						getBaseContext(), LUNCH, intentAlarm,
						PendingIntent.FLAG_UPDATE_CURRENT));
				lunchBtn.setText("Lunch not set");
				break;
			case R.id.cancelSnackBtn:
				alarmManager.cancel(PendingIntent.getBroadcast(
						getBaseContext(), SNACK, intentAlarm,
						PendingIntent.FLAG_UPDATE_CURRENT));
				snackBtn.setText("Snack not set");
				break;
			case R.id.cancelDinnerBtn:
				alarmManager.cancel(PendingIntent.getBroadcast(
						getBaseContext(), DINNER, intentAlarm,
						PendingIntent.FLAG_UPDATE_CURRENT));
				dinnerBtn.setText("Dinner not set");
				break;
		}
	}

}
