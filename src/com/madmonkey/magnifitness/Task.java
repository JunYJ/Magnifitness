package com.madmonkey.magnifitness;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

public class Task extends Fragment {
	SharedPreferences userSP;
	TextView taskToComplete;
	
	protected static ProfilePictureView profilePictureView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		userSP = this.getActivity().getSharedPreferences(
				FacebookLogin.filename, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.task, container, false);
		
		taskToComplete = (TextView) view.findViewById(R.id.taskToComplete);
		taskToComplete.setText("Hi " + userSP.getString("name", "")
				+ ", task to complete today!");

		profilePictureView = (ProfilePictureView) view
				.findViewById(R.id.userProfilePicture);
		profilePictureView.setProfileId(userSP.getString("userid", ""));
		profilePictureView.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
					{
					startActivity(new Intent(getActivity(), SetupUserDetails.class));
					
					}
			});
		
		
		return view;
	}

	public static ProfilePictureView getProfilePic() {
		return profilePictureView;
	}
}
