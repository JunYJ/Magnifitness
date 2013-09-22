package com.madmonkey.magnifitness;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SelectionFragment extends Fragment 
{
	private static final String TAG = "SelectionFragment";
	protected static TextView userInfo;
	protected static Button setupUserBtn;
	//Set-up the view from the layout
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.selection, container, false);
		Log.i(TAG, "SELECTION FRAGMENT");
		userInfo = (TextView) view.findViewById(R.id.txt);
		setupUserBtn = (Button) view.findViewById(R.id.setupUserBtn);
		/*userInfo.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Bundle bundle = new Bundle();
				bundle.putInt("myData", x);
				Intent nextScreen = new Intent(getActivity(), SetupUserDetails.class);
				nextScreen.putExtras(bundle);
				startActivityForResult(nextScreen, 1);
				
			}
		});*/
		//String name = getArguments().getString("name");
		//System.out.println("NAME: " + name);
		
		return view;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK)
		{
			System.out.println("Intent: " + data.getIntExtra("name", 0));
			System.out.println("Bundle: " + data.getBundleExtra("name"));
		}
	}
	
}
