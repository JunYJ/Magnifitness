package com.madmonkey.magnifitness;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StepCount extends Fragment
{
	SharedPreferences userSP;

	private TextView mStepValueView;
	private TextView mPaceValueView;
	private TextView mDistanceValueView;
	private TextView mSpeedValueView;
	private TextView mCaloriesValueView;
	TextView mDesiredPaceView;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		userSP = this.getActivity().getSharedPreferences(
				FacebookLogin.filename, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.pedometer, container, false);
		
		return view;
	}
}
