package com.madmonkey.magnifitness.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		// TODO Auto-generated method stub

		// here you can start an activity or service depending on your need
		// for ex you can start an activity to vibrate phone or to ring the
		// phone

		/* String phoneNumberReciver = "9718202185";// phone number to which SMS
		 * to // be send String message =
		 * "Hi I will be there later, See You soon";// message to // send
		 * SmsManager sms = SmsManager.getDefault();
		 * sms.sendTextMessage(phoneNumberReciver, null, message, null, null);
		 * // Show the toast like in above screen shot Toast.makeText(context,
		 * "Alarm Triggered and SMS Sent", Toast.LENGTH_LONG).show(); */
		Toast.makeText(context, "Alarm Triggered", Toast.LENGTH_LONG).show();
		Uri notification = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(context, notification);

		r.play();
	}

}