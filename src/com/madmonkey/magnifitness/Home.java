package com.madmonkey.magnifitness;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.widget.ProfilePictureView;
import com.madmonkey.magnifitness.ViewHistoryEntryFragment.OnRefreshListener;
import com.madmonkey.magnifitness.util.DatabaseHandler;
import com.madmonkey.magnifitnessClass.MealEntry;
import com.madmonkey.magnifitnessClass.User;

public class Home extends FragmentActivity implements OnPageChangeListener
{

	/** The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}. */
	SectionsPagerAdapter	mSectionsPagerAdapter;

	/** The {@link ViewPager} that will host the section contents. */
	ViewPager				mViewPager;
	MenuItem				logOut;
	MenuItem				Reminder;
	Session					s;
	PackageManager			manager;
	User					user;
	SharedPreferences		userSP;
	Fragment				currentFragment;
	TextView				userInfo;
	TextView				bmiTxt;

	DatabaseHandler			db;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		initialization();
	}

	private void initialization()
	{
		db = new DatabaseHandler(this);

		userSP = getSharedPreferences(FacebookLogin.filename, MODE_PRIVATE);

		// Enable Home Button
		/* final ActionBar actionBar = getActionBar();
		 * actionBar.setDisplayHomeAsUpEnabled(true); */

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		s = Session.getActiveSession();
		manager = getPackageManager();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO Auto-generated method stub
		logOut = menu.add(R.string.logOut)
				.setIcon(R.drawable.com_facebook_icon);
		Reminder = menu.add(R.string.Reminder);
		// Pedometer = menu.add(R.string.Pedometer);
		return super.onCreateOptionsMenu(menu);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				// This is called when the Home (Up) button is pressed in the
				// action
				// bar.
				// Create a simple intent that starts the hierarchical parent
				// activity and
				// use NavUtils in the Support Package to ensure proper handling
				// of
				// Up.
				Intent upIntent = new Intent(this, FacebookLogin.class);
				if (NavUtils.shouldUpRecreateTask(this, upIntent))
				{
					// This activity is not part of the application's task, so
					// create a new task
					// with a synthesized back stack.
					TaskStackBuilder.from(this)
					// If there are ancestor activities, they should be added
					// here.
							.addNextIntent(upIntent).startActivities();
					finish();
				}
				else
				{
					// This activity is part of the application's task, so
					// simply
					// navigate up to the hierarchical parent activity.
					NavUtils.navigateUpTo(this, upIntent);
				}
				return true;
		}

		if (item.equals(logOut))
		{
			// Fragment settings = new Settings();
			Toast.makeText(getApplicationContext(), "LOGOUT",
					Toast.LENGTH_SHORT).show();
			Log.i("LOGOUT", "LOGOUT");
			finishAffinity();
			// Fragment settings = findFragmentById(R.id.userSettingsFragment);
			s.closeAndClearTokenInformation();
			return true;
		}
		else if (item.equals(Reminder))
		{
			Toast.makeText(getApplicationContext(), "Reminder",
					Toast.LENGTH_SHORT).show();
			startActivity(new Intent(this, ReminderActivity.class));
		}

		return super.onOptionsItemSelected(item);
	}

	/** A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages. */
	public class SectionsPagerAdapter extends FragmentPagerAdapter
	{

		public SectionsPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int position)
		{
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			currentFragment = new Fragment();
			if (position == 0)
			{
				currentFragment = new Task();
			}

			else if (position == 1)
			{
				currentFragment = new MealLogBook();
				Bundle bundle = new Bundle();
				loadFromPref();
				bundle.putInt("calorieCap", user.getTotalDailyCalorieNeeds());

				// Current date
				Calendar c = Calendar.getInstance();
				DateFormatSymbols dfs = new DateFormatSymbols();
				String[] months = dfs.getMonths();
				String date = "";

				if (c.get(Calendar.DAY_OF_MONTH) == 1
						|| c.get(Calendar.DAY_OF_MONTH) == 11
						|| c.get(Calendar.DAY_OF_MONTH) == 21
						|| c.get(Calendar.DAY_OF_MONTH) == 31)
				{
					date = "" + c.get(Calendar.DAY_OF_MONTH) + "st "
							+ months[(c.get(Calendar.MONTH))] + " "
							+ c.get(Calendar.YEAR);
				}
				else if (c.get(Calendar.DAY_OF_MONTH) == 2
						|| c.get(Calendar.DAY_OF_MONTH) == 12
						|| c.get(Calendar.DAY_OF_MONTH) == 22)
				{
					date = "" + c.get(Calendar.DAY_OF_MONTH) + "nd "
							+ months[(c.get(Calendar.MONTH))] + " "
							+ c.get(Calendar.YEAR);
				}
				else if (c.get(Calendar.DAY_OF_MONTH) == 3
						|| c.get(Calendar.DAY_OF_MONTH) == 13
						|| c.get(Calendar.DAY_OF_MONTH) == 23)
				{
					date = "" + c.get(Calendar.DAY_OF_MONTH) + "rd "
							+ months[(c.get(Calendar.MONTH))] + " "
							+ c.get(Calendar.YEAR);
				}
				else
				{
					date = "" + c.get(Calendar.DAY_OF_MONTH) + "th "
							+ months[(c.get(Calendar.MONTH))] + " "
							+ c.get(Calendar.YEAR);
				}

				// Get meal entries of the day
				ArrayList<MealEntry> mealEntryList = null;
				mealEntryList = (ArrayList<MealEntry>) db
						.getTodayMealEntry(date);

				// Total calorie consumed of the day
				double todayCalorie = 0.0;
				if (mealEntryList != null)
				{
					for (int i = 0; i < mealEntryList.size(); i++)
					{
						todayCalorie += mealEntryList.get(i).getTotalCalorie();
					}
				}

				bundle.putDouble("todayCalorie", todayCalorie);

				currentFragment.setArguments(bundle);
			}

			else if (position == 2)
			{
				currentFragment = new ViewHistoryEntryFragment();

			}

			else if (position == 3)
			{
				currentFragment = new PedometerFragment();
			}

			else
			{
				currentFragment = new DummySectionFragment();
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER,
						position + 1);
				currentFragment.setArguments(args);
			}

			return currentFragment;
		}

		@Override
		public int getCount()
		{
			// Show 4 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			Locale l = Locale.getDefault();
			switch (position)
			{
				case 0:
					return getString(R.string.title_section1).toUpperCase(l);
				case 1:
					return getString(R.string.title_section2).toUpperCase(l);
				case 2:
					return getString(R.string.title_section3).toUpperCase(l);
				case 3:
					return getString(R.string.title_section4).toUpperCase(l);
			}
			return null;
		}
	}

	/** A dummy fragment representing a section of the app, but that simply
	 * displays dummy text. */
	public static class DummySectionFragment extends Fragment
	{
		/** The fragment argument representing the section number for this
		 * fragment. */
		public static final String	ARG_SECTION_NUMBER	= "section_number";

		public DummySectionFragment()
		{
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_home_dummy,
					container, false);
			/* TextView dummyTextView = (TextView) rootView
			 * .findViewById(R.id.section_label);
			 * dummyTextView.setText("I am page " +
			 * Integer.toString(getArguments().getInt( ARG_SECTION_NUMBER))); */
			Button pedometerBtn = (Button) rootView
					.findViewById(R.id.activatePedometerBtn);
			pedometerBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub

					getActivity().finish();

					// startActivity(new Intent(getActivity(),
					// Pedometer.class));
					Intent i = new Intent();
					i.setAction("name.bagi.levente.PEDOMETER");
					startActivity(i);

				}

			});

			return rootView;
		}
	}

	/* public static class Settings extends Fragment { public Settings() { }
	 * public View onCreateView(LayoutInflater inflater, ViewGroup container,
	 * Bundle savedInstanceState) { View settings =
	 * inflater.inflate(R.layout.com_facebook_usersettingsfragment, container,
	 * false); return settings; } } */

	public void loadFromPref()
	{
		String username, email, gender;
		int age, weight, height, lvOfActiveness;

		if (userSP.getBoolean("userCreated", false))
		{
			username = userSP.getString("name", "unknown");
			email = userSP.getString("email", "unknown");
			gender = userSP.getString("gender", "unknown");
			age = userSP.getInt("age", 0);
			lvOfActiveness = userSP.getInt("lvOfActiveness", 0);
			weight = userSP.getInt("weight", 1);
			height = userSP.getInt("height", 1);

			user = new User(username, age, email, gender, weight, height,
					lvOfActiveness);

		}

	}

	public String buildSummaryString()
	{
		StringBuilder userInfo = new StringBuilder("");
		// SelectionFragment.profilePictureView.setProfileId(user.getId());

		// Task.getProfilePic().setProfileId(user.getId());
		// USER NAME
		// Example: typed access (name)
		// - no special permissions required
		// userInfo.append(String.format("Name: %s\n\n", user.getName()));
		userInfo.append(String.format("Name: %s\n\n",
				userSP.getString("name", "")));
		// Example: typed access (birthday)
		// - requires user_birthday permissions
		// userInfo.append(String.format("Birthday: %s\n\n",
		// user.getBirthday()));

		// AGE
		userInfo.append(String.format("Age: %s\n\n", userSP.getInt("age", 0)));

		// GENDER
		userInfo.append(String.format("Gender: %s\n\n",
				userSP.getString("gender", "")));

		// EMAIL (sharedPreference)
		userInfo.append(String.format("Email: %s\n\n",
				userSP.getString("email", "")));

		// WEIGHT
		userInfo.append(String.format("Weight: %s kg \n\n",
				userSP.getInt("weight", 0)));
		// HEIGHT
		userInfo.append(String.format("Height: %s cm \n\n",
				userSP.getInt("height", 0)));
		// BMI
		/* userInfo.append(String.format("BMI: %s\n\n", userSP.getString("bmi",
		 * ""))); */
		// BMR
		/* userInfo.append(String.format("BMR: %s\n\n", userSP.getString("bmr",
		 * ""))); */

		// IDEAL WEIGHT
		userInfo.append(String.format("Ideal Weight: %s kg \n",
				userSP.getInt("idealWeight", 0)));

		// BMI
		StringBuilder bmi = new StringBuilder("");
		double bmiValue = Double.parseDouble(userSP.getString("bmi", "0.0"));

		if (bmiValue >= 30.0)
		{
			bmi.append(String.format("BMI: %s\n", userSP.getString("bmi", ""))
					+ " (Obese)");
			bmiTxt.setTextColor(Color.RED);
		}
		else if (bmiValue < 18.5)
		{
			bmi.append(String.format("BMI: %s\n", userSP.getString("bmi", ""))
					+ " (Underweight)");
			bmiTxt.setTextColor(Color.RED);
		}

		else if (bmiValue < 30.0 && bmiValue > 24.5)
		{
			bmi.append(String.format("BMI: %s\n", userSP.getString("bmi", ""))
					+ " (Overweight)");
			bmiTxt.setTextColor(Color.rgb(255, 165, 0));
		}
		else
		{
			bmi.append(String.format("BMI: %s\n", userSP.getString("bmi", "")));
			bmiTxt.setTextColor(Color.GREEN);
		}

		bmiTxt.setText(bmi);

		return userInfo.toString();
	}

	// Show summary window
	public void editProfile(View v)
	{
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);

		LayoutInflater inflater = getLayoutInflater();
		View summary = inflater.inflate(R.layout.summary, null);
		helpBuilder.setView(summary);

		userInfo = (TextView) summary.findViewById(R.id.txt);
		bmiTxt = (TextView) summary.findViewById(R.id.bmiTxt);
		bmiTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Toast.makeText(
						getApplicationContext(),
						"Obesity (>=30) \nOverweight (25-29.9) \nNormal (18.5-24.9) \nUnderweight (<18.5)",
						Toast.LENGTH_LONG).show();
			}
		});

		userInfo.setText(buildSummaryString());

		ProfilePictureView summaryProfilePictureView = (ProfilePictureView) summary
				.findViewById(R.id.friendProfilePicture);
		summaryProfilePictureView.setProfileId(userSP.getString("userid", ""));

		helpBuilder.setPositiveButton("Done",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which)
					{

					}
				});

		// Remember, create doesn't show the dialog
		AlertDialog helpDialog = helpBuilder.create();

		helpDialog.show();

	}

	// Edit profile (Setup User Details.java)
	public void edit(View v)
	{
		userSP.edit().putBoolean("edittingProfile", true).commit();
		Intent i = new Intent(this, SetupUserDetails.class);
		startActivityForResult(i, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1)
		{
			if (resultCode == RESULT_OK)
			{
				userInfo.setText(buildSummaryString());
			}
		}
	}

	@Override
	protected void onResume()
	{
		mSectionsPagerAdapter.notifyDataSetChanged();
		super.onResume();
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();

		finish();
	}

	@Override
	public void onPageScrollStateChanged(int arg0)
	{
		if (arg0 == 2)// View
		{

			Toast.makeText(getApplicationContext(), "HEY YO! OnPageSelected",
					Toast.LENGTH_LONG).show();

		}

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{
		if (arg0 == 2)// View
		{

			Toast.makeText(getApplicationContext(), "HEY YO! OnPageSelected",
					Toast.LENGTH_LONG).show();

		}
	}

	@Override
	public void onPageSelected(int pos)
	{

		((OnRefreshListener) mSectionsPagerAdapter.getItem(pos)).onRefresh();

	}

}
