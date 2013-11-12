package com.madmonkey.magnifitness;

import java.util.Locale;

import com.facebook.Session;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import name.bagi.levente.pedometer.Pedometer;
public class Home extends FragmentActivity
{

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	MenuItem logOut;
	Session s;
	PackageManager manager;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		//Enable Home Button
		/*final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);*/

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
		logOut = menu.add(R.string.logOut).setIcon(R.drawable.com_facebook_icon);
		return super.onCreateOptionsMenu(menu);
	}

	@SuppressWarnings("deprecation")
	@Override
    public boolean onOptionsItemSelected(MenuItem item) 
	{
        switch (item.getItemId()) 
        {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, FacebookLogin.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) 
                {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } 
                else
                {
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        
        if (item.equals(logOut))
		{
        	//Fragment settings = new Settings();
        	Toast.makeText(getApplicationContext(), "LOGOUT", Toast.LENGTH_LONG).show();
        	System.out.println("LOGOUT");
        	finishAffinity();
        	//Fragment settings = findFragmentById(R.id.userSettingsFragment);
        	s.closeAndClearTokenInformation();
			return true;
		}
		
        return super.onOptionsItemSelected(item);
    }

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
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
			Fragment fragment;
			if(position == 0)
			{
				fragment = new Task();
			}
			
			else if(position == 1)
			{
				fragment = new MealLogBook();
			}
			
			else
			{
				fragment = new DummySectionFragment();
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragment.setArguments(args);
			}
			
			
			return fragment;
		}

		@Override
		public int getCount()
		{
			// Show 3 total pages.
			return 3;
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
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment 
	{
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() 
		{
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View rootView = inflater.inflate(R.layout.fragment_home_dummy,
					container, false);
			/*TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText("I am page " + Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));*/
			Button pedometerBtn = (Button) rootView.findViewById(R.id.activatePedometerBtn);
			pedometerBtn.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					/*Intent i = new Intent();
					i.setComponent(new ComponentName("name.bagi", "name.bagi.levente.pedometer"));
					startActivity(i);*/
					
					/*Intent i = new Intent(Intent.ACTION_MAIN,null);
					i.addCategory(Intent.CATEGORY_LAUNCHER);
					final ComponentName cn = new ComponentName("name.bagi.levente.pedometer","name.bagi.levente.pedometer.Pedometer");
					i.setComponent(cn);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					//Intent i = new Intent(getApplicationContext(), ShowMapActivity.class);
					startActivity(i);*/
					
					/*Intent i = new Intent(Intent.ACTION_MAIN);
					i = getActivity().getPackageManager().getLaunchIntentForPackage("name.bagi.levente.pedometer");
					i.addCategory(Intent.CATEGORY_LAUNCHER);
					startActivity(i);*/
					getActivity().finish();
					
					startActivity(new Intent(getActivity(), Pedometer.class));
					
				}
				
			});
			
			/*View rootView = inflater.inflate(R.layout.fragment_home, container, false);*/
			
					
			return rootView;
		}
	}
	
	/*public static class Settings extends Fragment
	{
		
		public Settings()
		{
			
		}
		
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View settings = inflater.inflate(R.layout.com_facebook_usersettingsfragment, container, false);
			return settings;
		}
	}*/

}
