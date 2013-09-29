package com.madmonkey.magnifitness;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Task extends Fragment
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		/*View rootView = inflater.inflate(R.layout.fragment_home_dummy,
				container, false);
		TextView dummyTextView = (TextView) rootView
				.findViewById(R.id.section_label);
		dummyTextView.setText("I am page " + Integer.toString(getArguments().getInt(
				ARG_SECTION_NUMBER)));*/
		//if(ARG_SECTION_NUMBER == "1")
		
		View rootView = inflater.inflate(R.layout.fragment_home,
						container, false);
		
				
		return rootView;
	}

}
