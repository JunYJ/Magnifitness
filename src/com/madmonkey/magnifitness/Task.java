package com.madmonkey.magnifitness;

import com.facebook.widget.ProfilePictureView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Task extends Fragment
{
	SharedPreferences userSP;
	TextView taskToComplete;
	protected static ProfilePictureView profilePictureView;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		userSP = this.getActivity().getSharedPreferences(FacebookLogin.filename, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_home,
						container, false);
		taskToComplete = (TextView) view.findViewById(R.id.taskToComplete);
		taskToComplete.setText("Hi " + userSP.getString("name", "") + ", task to complete today!");
		
		profilePictureView = (ProfilePictureView) view.findViewById(R.id.userProfilePicture);
		return view;
	}
	
	public static ProfilePictureView getProfilePic()
	{
		return profilePictureView;
	}
}
