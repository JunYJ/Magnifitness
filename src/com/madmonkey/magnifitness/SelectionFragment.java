package com.madmonkey.magnifitness;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

public class SelectionFragment extends Fragment 
{
	private static final String TAG = "SelectionFragment";
	protected static TextView userInfo;
	protected static Button setupUserBtn;
	protected static ProfilePictureView profilePictureView;
	SharedPreferences userSP;
	
	//Set-up the view from the layout
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		userSP = this.getActivity().getSharedPreferences(FacebookLogin.filename, 0);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.selection, container, false);
		
		Log.i(TAG, "SELECTION FRAGMENT");
		userInfo = (TextView) view.findViewById(R.id.txt);
		
		setupUserBtn = (Button) view.findViewById(R.id.setupUserBtn);
		setupUserBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), SetupUserDetails.class));
			}
		});
		
		profilePictureView = (ProfilePictureView) view.findViewById(R.id.friendProfilePicture);
		
	
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
