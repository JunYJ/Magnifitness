package com.madmonkey.magnifitness;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.logging.Logger;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.madmonkey.magnifitness.service.IStepService;
import com.madmonkey.magnifitness.service.IStepServiceCallback;
import com.madmonkey.magnifitness.service.StepDetector;
import com.madmonkey.magnifitness.service.StepService;
import com.madmonkey.magnifitness.util.DatabaseHandler;
import com.madmonkey.magnifitness.util.MessageUtilities;
import com.madmonkey.magnifitnessClass.Pedometer;

public class PedometerFragment extends Fragment
{
	private static final Logger					logger				= Logger.getLogger(PedometerFragment.class
																			.getSimpleName());

	private static ToggleButton					startStopButton		= null;
	private static ArrayList<String>			sensArrayList		= null;
	private static ArrayAdapter<CharSequence>	modesAdapter		= null;
	private static TextView						stepText			= null;
	private static TextView						distanceText		= null;

	private static PowerManager					powerManager		= null;
	private static WakeLock						wakeLock			= null;

	public static IStepService					mService			= null;
	public static Intent						stepServiceIntent	= null;

	private static int							sensitivity			= 100;

	int											idx;
	static int									lastRecordedStep;
	static int									currentStep;
	static int									lastRecordedDistance;
	static int									distance;
	static int									stepLength;
	Spinner										sensSpinner;
	static NumberPicker							stepLengthPicker;
	static DatabaseHandler						db;
	static Pedometer							pedo;
	static SharedPreferences					userSP;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// setRetainInstance(true);
		userSP = getActivity().getSharedPreferences(FacebookLogin.filename, 0);
		powerManager = (PowerManager) getActivity().getSystemService(
				Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				"Pedometer");

		if (stepServiceIntent == null)
		{
			Bundle extras = new Bundle();
			extras.putInt("int", 1);
			stepServiceIntent = new Intent(getActivity(), StepService.class);
			stepServiceIntent.putExtras(extras);
		}

		sensitivity = userSP.getInt("sensitivity", 100);
		stepLength = userSP.getInt("stepLength", 20);

		String sensStr = String.valueOf(sensitivity);
		idx = 0;

		if (sensArrayList == null)
		{
			String[] sensArray = getResources().getStringArray(
					R.array.sensitivity);
			sensArrayList = new ArrayList<String>(Arrays.asList(sensArray));
		}
		if (sensArrayList.contains(sensStr))
		{
			idx = sensArrayList.indexOf(sensStr);
			// sensSpinner.setSelection(idx);
		}

		lastRecordedStep = 0;
		currentStep = 0;
		lastRecordedDistance = 0;
		distance = 0;

		Calendar date = Calendar.getInstance();
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		String dateStr = "" + date.get(Calendar.DAY_OF_MONTH) + "th "
				+ months[(date.get(Calendar.MONTH))] + " "
				+ date.get(Calendar.YEAR);

		pedo = null;

		db = new DatabaseHandler(getActivity());
		pedo = db.getPedometerState(dateStr);

		if (pedo != null)
		{
			lastRecordedStep = pedo.getStep();
			// currentStep = pedo.getStep();
			lastRecordedDistance = pedo.getDistance();
			// distance = pedo.getDistance();
			Log.i("LAST RECORDED STEP", lastRecordedStep + "");
			Log.i("LAST RECORDED DISTANCE", lastRecordedDistance + "");
		}
		else
		{
			pedo = new Pedometer();
			db.addPedometerState(pedo);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.pedometer, container, false);

		startStopButton = (ToggleButton) view
				.findViewById(R.id.StartStopButton);
		startStopButton.setOnCheckedChangeListener(startStopListener);

		sensSpinner = (Spinner) view
				.findViewById(R.id.input_sensitivity_spinner);
		modesAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.sensitivity, android.R.layout.simple_spinner_item);
		modesAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sensSpinner.setOnItemSelectedListener(sensListener);
		sensSpinner.setAdapter(modesAdapter);
		sensSpinner.setSelection(idx);

		stepLengthPicker = (NumberPicker) view
				.findViewById(R.id.stepLengthPicker);
		stepLengthPicker.setMaxValue(100);
		stepLengthPicker.setMinValue(0);
		stepLengthPicker.setValue(stepLength);

		stepText = (TextView) view.findViewById(R.id.stepTxt);
		stepText.setText("Steps = " + lastRecordedStep);
		distanceText = (TextView) view.findViewById(R.id.distanceTxt);

		if (lastRecordedDistance >= 100 && lastRecordedDistance < 100000)
		{
			distanceText.setText("Distance = " + (lastRecordedDistance / 100)
					+ "m " + (lastRecordedDistance % 100) + "cm");
		}
		else if (lastRecordedDistance >= 100000)
		{
			int m = lastRecordedDistance / 100;
			int cm = lastRecordedDistance % 100;
			int km = m / 1000;
			distanceText.setText("Distance = " + km + "km " + m + "m " + cm
					+ "cm");
		}
		else
			distanceText.setText("Distance = " + lastRecordedDistance + "cm");

		return view;
	}

	/** {@inheritDoc} */
	@Override
	public void onStart()
	{
		super.onStart();

		if (!wakeLock.isHeld())
			wakeLock.acquire();

		// Bind without starting the service
		try
		{
			getActivity().bindService(stepServiceIntent, mConnection, 0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void onPause()
	{
		super.onPause();

		if (wakeLock.isHeld())
			wakeLock.release();

		unbindStepService();
	}

	private OnItemSelectedListener	sensListener	= new OnItemSelectedListener() {

														/** {@inheritDoc} */
														@Override
														public void onItemSelected(
																AdapterView<?> arg0,
																View arg1,
																int arg2,
																long arg3)
														{
															CharSequence seq = modesAdapter
																	.getItem(arg2);
															String sensString = String
																	.valueOf(seq);
															if (sensString != null)
															{
																sensitivity = Integer
																		.parseInt(sensString);
																StepDetector
																		.setSensitivity(sensitivity);
																userSP.edit()
																		.putInt("sensitivity",
																				sensitivity)
																		.commit();
															}
														}

														/** {@inheritDoc} */
														@Override
														public void onNothingSelected(
																AdapterView<?> arg0)
														{
															// Ignore
														}
													};

	/** {@inheritDoc} */
	/* @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
	 * (keyCode == KeyEvent.KEYCODE_BACK) { try { if (mService != null &&
	 * mService.isRunning()) { MessageUtilities.confirmUser(Demo.this,
	 * "Exit App without stopping pedometer?", yesExitClick, null); } else {
	 * stop();
	 * 
	 * finish(); } } catch (RemoteException e) { e.printStackTrace(); return
	 * false; } return true; } return super.onKeyDown(keyCode, event); } */

	/** {@inheritDoc} */
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
	}

	private OnCheckedChangeListener							startStopListener	= new OnCheckedChangeListener() {

																					/** {@inheritDoc} */
																					@Override
																					public void onCheckedChanged(
																							CompoundButton buttonView,
																							boolean isChecked)
																					{
																						boolean serviceIsRunning = false;
																						try
																						{
																							if (mService != null)
																								serviceIsRunning = mService
																										.isRunning();
																						}
																						catch (RemoteException e)
																						{
																							e.printStackTrace();
																						}
																						if (isChecked
																								&& !serviceIsRunning)
																						{
																							start();
																						}
																						else if (!isChecked
																								&& serviceIsRunning)
																						{
																							MessageUtilities
																									.confirmUser(
																											getActivity(),
																											"Stop the pedometer?",
																											yesStopClick,
																											noStopClick);
																						}
																					}
																				};

	private DialogInterface.OnClickListener					yesStopClick		= new DialogInterface.OnClickListener() {

																					/** {@inheritDoc} */
																					@Override
																					public void onClick(
																							DialogInterface dialog,
																							int which)
																					{
																						stop();
																					}
																				};

	private static final DialogInterface.OnClickListener	noStopClick			= new DialogInterface.OnClickListener() {

																					/** {@inheritDoc} */
																					@Override
																					public void onClick(
																							DialogInterface dialog,
																							int which)
																					{
																						if (mService != null)
																							try
																							{
																								startStopButton
																										.setChecked(mService
																												.isRunning());
																							}
																							catch (RemoteException e)
																							{
																								e.printStackTrace();
																							}
																					}
																				};

	private DialogInterface.OnClickListener					yesExitClick		= new DialogInterface.OnClickListener() {

																					/** {@inheritDoc} */
																					@Override
																					public void onClick(
																							DialogInterface dialog,
																							int which)
																					{
																						unbindStepService();

																						getActivity()
																								.finish();
																					}
																				};

	private void start()
	{
		logger.info("start");
		Calendar date = Calendar.getInstance();
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		String dateStr = "" + date.get(Calendar.DAY_OF_MONTH) + "th "
				+ months[(date.get(Calendar.MONTH))] + " "
				+ date.get(Calendar.YEAR);
		db = new DatabaseHandler(getActivity());
		pedo = db.getPedometerState(dateStr);
		startStepService();
		bindStepService();
	}

	private void stop()
	{
		logger.info("stop");

		unbindStepService();
		stopStepService();
	}

	private void startStepService()
	{
		try
		{
			getActivity().startService(stepServiceIntent);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void stopStepService()
	{
		try
		{
			getActivity().stopService(stepServiceIntent);
		}
		catch (Exception e)
		{
			// Ignore
		}
	}

	private void bindStepService()
	{
		try
		{
			getActivity().bindService(stepServiceIntent, mConnection,
					Context.BIND_AUTO_CREATE);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void unbindStepService()
	{
		try
		{
			getActivity().unbindService(mConnection);
		}
		catch (Exception e)
		{
			// Ignore
		}
	}

	private static final Handler					handler		= new Handler() {

																	public void handleMessage(
																			Message msg)
																	{
																		currentStep = msg.arg1;
																		currentStep = 1;
																		stepText.setText("Steps = "
																				+ (currentStep + lastRecordedStep));
																		distance = currentStep
																				* stepLengthPicker
																						.getValue();
																		userSP.edit()
																				.putInt("stepLength",
																						stepLength)
																				.commit();

																		if (pedo != null)
																		{
																			pedo.setStep(currentStep
																					+ lastRecordedStep);
																			lastRecordedStep += currentStep;
																			pedo.setDistance(distance
																					+ lastRecordedDistance);
																			lastRecordedDistance += distance;
																			Log.i("PEDOMETER",
																					"update");
																			db.updatePedometerState(pedo);
																		}

																		if ((distance + lastRecordedDistance) >= 100
																				&& (distance + lastRecordedDistance) < 100000)
																		{
																			distanceText
																					.setText("Distance = "
																							+ ((distance + lastRecordedDistance) / 100)
																							+ "m "
																							+ ((distance + lastRecordedDistance) % 100)
																							+ "cm");
																		}
																		else if ((distance + lastRecordedDistance) >= 100000)
																		{
																			int m = (distance + lastRecordedDistance) / 100;
																			int cm = (distance + lastRecordedDistance) % 100;
																			int km = m / 1000;
																			distanceText
																					.setText("Distance = "
																							+ km
																							+ "km "
																							+ m
																							+ "m "
																							+ cm
																							+ "cm");
																		}
																		else
																			distanceText
																					.setText("Distance = "
																							+ (distance + lastRecordedDistance)
																							+ "cm");

																	}
																};

	private static final IStepServiceCallback.Stub	mCallback	= new IStepServiceCallback.Stub() {

																	@Override
																	public IBinder asBinder()
																	{
																		return mCallback;
																	}

																	@Override
																	public void stepsChanged(
																			int value)
																			throws RemoteException
																	{
																		logger.info("Steps="
																				+ value);
																		Message msg = handler
																				.obtainMessage();
																		msg.arg1 = value;
																		handler.sendMessage(msg);
																	}
																};

	private static final ServiceConnection			mConnection	= new ServiceConnection() {

																	/** {@inheritDoc} */
																	@Override
																	public void onServiceConnected(
																			ComponentName className,
																			IBinder service)
																	{
																		logger.info("onServiceConnected()");
																		mService = IStepService.Stub
																				.asInterface(service);
																		try
																		{
																			mService.registerCallback(mCallback);
																			mService.setSensitivity(sensitivity);
																			startStopButton
																					.setChecked(mService
																							.isRunning());
																		}
																		catch (RemoteException e)
																		{
																			e.printStackTrace();
																		}
																	}

																	/** {@inheritDoc} */
																	@Override
																	public void onServiceDisconnected(
																			ComponentName className)
																	{
																		logger.info("onServiceDisconnected()");
																		try
																		{
																			startStopButton
																					.setChecked(mService
																							.isRunning());
																		}
																		catch (RemoteException e)
																		{
																			e.printStackTrace();
																		}
																		mService = null;
																	}
																};
}
