package com.madmonkey.magnifitness;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.madmonkey.magnifitness.util.AchievementAdapter;
import com.madmonkey.magnifitnessClass.Achievement;
import com.madmonkey.magnifitnessClass.CalorieAchievement;
import com.madmonkey.magnifitnessClass.GeneralAchievement;
import com.madmonkey.magnifitnessClass.PedoAchievement;

public class Task extends Fragment
{
	SharedPreferences										userSP;
	TextView														taskToComplete;
	ListView														listView;
	ArrayList<Achievement>							achievementList;
	AchievementAdapter aa;

	protected static ProfilePictureView	profilePictureView;

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
		View view = inflater.inflate(R.layout.task, container, false);

		listView = (ListView) view.findViewById(R.id.list);
		doAchievements();

		listView.setOnItemClickListener(new OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> l, View v, int pos, long id)
					{
					final Achievement a = (Achievement) l.getAdapter().getItem(pos);

					String text = a.getDescription();
					String statusStr = "Status: " + (a.getStatus()? "Completed" : "Not Completed");
					String rewardStr = "Reward: " + a.getReward();

					AlertDialog.Builder adb = new Builder(getActivity());
					adb.setTitle(a.getTitle());
					adb.setIcon((a.getStatus() == Achievement.COMPLETED) ? R.drawable.achievenment_star_icon
							: R.drawable.achievenment_star_icon_grey); //IF completed set icon to respective color
					
					adb.setMessage(text + "\n" + rewardStr + "\n" + statusStr);
					adb.setPositiveButton("Complete it now", new DialogInterface.OnClickListener()
						{
							
							@Override
							public void onClick(DialogInterface dialog, int which)
								{
									a.setCompleted();
									aa.notifyDataSetChanged();
									dialog.dismiss();
								}
						});
					adb.show();
					}
			});

		taskToComplete = (TextView) view.findViewById(R.id.taskToComplete);
		taskToComplete.setText("Hi " + userSP.getString("name", "")
				+ ", task to complete today!");

		profilePictureView = (ProfilePictureView) view
				.findViewById(R.id.userProfilePicture);
		profilePictureView.setProfileId(userSP.getString("userid", ""));

		return view;
		}

	private void doAchievements()
		{
		achievementList = new ArrayList<Achievement>();

		GeneralAchievement ga = new GeneralAchievement("First Time Login", 15,
				"First time login into Magnifitness");
		ga.setCompleted();
		GeneralAchievement ga1 = new GeneralAchievement("First Meal Logged", 15,
				"Logged your first meal");
		GeneralAchievement ga2 = new GeneralAchievement("Pedometer Started", 15,
				"Pedometer is running! Go take a walk!");
		GeneralAchievement ga3 = new GeneralAchievement("Profile Completed", 15,
				"Your profile is completed, ready to go");

		achievementList.add(ga);
		achievementList.add(ga1);
		achievementList.add(ga2);
		achievementList.add(ga3);

		PedoAchievement pa = new PedoAchievement("Walked 10,000 steps", 25,
				"Walked 10000 with the pedometer!", 10000);
		PedoAchievement pa1 = new PedoAchievement("Walked 25,000 steps", 25,
				"Walked 25000 with the pedometer!", 25000);
		PedoAchievement pa2 = new PedoAchievement("Walked 50,000 steps", 50,
				"Walked 50000 with the pedometer!", 50000);
		PedoAchievement pa3 = new PedoAchievement("Walked 50,000 steps", 50,
				"Walked 50000 with the pedometer!", 50000);
		PedoAchievement pa4 = new PedoAchievement("Walked 100,000 steps", 100,
				"Walked 100000 with the pedometer!", 100000);

		achievementList.add(pa);
		achievementList.add(pa1);
		achievementList.add(pa2);
		achievementList.add(pa3);
		achievementList.add(pa4);

		CalorieAchievement ca = new CalorieAchievement("2500 Calories Saved", 25,
				"You have saved 2500 Calories from your meal", 2500);
		CalorieAchievement ca2 = new CalorieAchievement("5000 Calories Saved", 50,
				"You have saved 5000 Calories from your meal", 5000);
		CalorieAchievement ca3 = new CalorieAchievement("10000 Calories Saved", 100,
				"You have saved 10000 Calories from your meal", 10000);
		CalorieAchievement ca4 = new CalorieAchievement("20000 Calories Saved", 125,
				"You have saved 20000 Calories from your meal", 20000);
		CalorieAchievement ca5 = new CalorieAchievement("50000 Calories Saved", 150,
				"You have saved 50000 Calories from your meal", 50000);

		achievementList.add(ca);
		achievementList.add(ca2);
		achievementList.add(ca3);
		achievementList.add(ca4);
		achievementList.add(ca5);

		
		aa = new AchievementAdapter(getActivity().getBaseContext(), achievementList);

		listView.setAdapter(aa);

		}

	public static ProfilePictureView getProfilePic()
		{
		return profilePictureView;
		}
}
