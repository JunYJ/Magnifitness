package com.madmonkey.magnifitness;

import java.util.Calendar;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

public class FacebookLogin extends FragmentActivity
{
	// Fragment index
	private static final int		LOGIN			= 0;
	// private static final int SELECTION = 1;
	private static final int		SETTINGS		= 1;
	private static final int		FRAGMENT_COUNT	= SETTINGS + 1;

	// flag to indicate a visible activity
	public static boolean			isResumed		= false;

	// helps to create, auto open (if applicable), save & restore
	// Active Session in a way that is similar to Android UI lifecycles
	private UiLifecycleHelper		uiHelper;

	// track the session & trigger a session state change listener
	// Session.StatusCallback() overrides call() & invokes
	// onSessionStateChange()
	private Session.StatusCallback	callBack		= new Session.StatusCallback() {
														@Override
														public void call(
																Session session,
																SessionState state,
																Exception exception)
														{
															onSessionStateChange(
																	session,
																	state,
																	exception);
														}
													};

	// top right Menu
	private MenuItem				logOut;

	// Array of fragment
	private Fragment[]				fragments		= new Fragment[FRAGMENT_COUNT];

	private static final String		TAG				= "Facebook Login";

	Button							setupUserBtn;
	SharedPreferences				userSP;
	public static String			filename		= "com.madmonkey.magnifitness.SharedPref";

	// flag to determine whether user account is existed
	boolean							userCreated;

	// hide fragments initially in onCreate
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		uiHelper = new UiLifecycleHelper(this, callBack);
		uiHelper.onCreate(savedInstanceState);
		setContentView(R.layout.facebook_login);

		// store fragments
		FragmentManager fm = getSupportFragmentManager();
		fragments[LOGIN] = fm.findFragmentById(R.id.loginFragment);
		fragments[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);

		// hide all fragment
		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++)
		{
			transaction.hide(fragments[i]);
		}
		transaction.commit();

		// initialize Shared Preferences
		// check whether user account is existed
		userSP = getSharedPreferences(FacebookLogin.filename, 0);
		userCreated = userSP.getBoolean("userCreated", false);

		// final ActionBar actionBar = getActionBar();
		// actionBar.setDisplayHomeAsUpEnabled(true);
	}

	// responsible for showing a given fragment & hiding all other
	// fragments
	private void showFragment(int fragmentIndex, boolean addToBackStack)
	{
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		for (int i = 0; i < fragments.length; i++)
		{
			if (i == fragmentIndex)
			{
				transaction.show(fragments[i]);
			}
			else
			{
				transaction.hide(fragments[i]);
			}
		}
		if (addToBackStack)
		{
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		uiHelper.onResume();
		isResumed = true;
	}

	@Override
	public void onPause()
	{
		super.onPause();
		uiHelper.onPause();
		isResumed = false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	// called due to state changes
	// shows relevant fragment based on user's authenticated state
	// fragment back stack is 1st cleared before the showFragment() is
	// called
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception)
	{
		// Only make changes if the activity is visible
		if (isResumed)
		{
			android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

			// Get the number of entries in the back stack
			int backStackSize = manager.getBackStackEntryCount();

			// Clear the back stack
			for (int i = 0; i < backStackSize; i++)
			{
				manager.popBackStack();
			}

			if (state.isOpened())
			{
				// If the session state is open --> show the authenticated
				// fragment
				Log.i(TAG, "Logged in...");

				// Do not store history of this activity
				final Intent i = new Intent(this, SetupUserDetails.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

				// Request user data and show the results
				Request meRequest = Request.newMeRequest(session,
						new GraphUserCallback() {

							@Override
							public void onCompleted(GraphUser user,
									Response response)
							{

								if (user != null)
								{
									// Display the parsed user info

									// Insert value below

									// FACEBOOK ID (used for showing profile
									// picture)
									userSP.edit()
											.putString("userid", user.getId())
											.commit();

									// NAME
									userSP.edit()
											.putString("name",
													user.getName().toString())
											.commit();

									// EMAIL
									userSP.edit()
											.putString("email",
													getEmail(getBaseContext()))
											.commit();

									// AGE
									Calendar c = Calendar.getInstance();

									userSP.edit()
											.putInt("age",
													c.get(Calendar.YEAR)
															- Integer
																	.parseInt(user
																			.getBirthday()
																			.substring(
																					user.getBirthday()
																							.length() - 4)))
											.commit();

									// GENDER
									String gender = user.asMap().get("gender")
											.toString();

									if (gender.equalsIgnoreCase("male"))
										gender = "Male";
									else if (gender.equalsIgnoreCase("female"))
										gender = "Female";

									userSP.edit().putString("gender", gender)
											.commit();

									if (userSP.getBoolean("userCreated", false) == false)
										startActivity(i);
								}

							}

						});

				meRequest.executeAsync();
				this.finish();
			}

			else if (state.isClosed())
			{
				Log.i(TAG, "Logged out...");
				// If the session state is close --> show the login
				// fragment
				showFragment(LOGIN, false);
			}
		}
	}

	// handle the case where fragments are newly instantiated
	@Override
	protected void onResumeFragments()
	{
		super.onResumeFragments();
		Session session = Session.getActiveSession();

		if (session != null && session.isOpened())
		{
			// if the session is already open --> try to show the selection
			// fragment

			if (userCreated == true)
			{
				startActivity(new Intent(this, Home.class));
				finish();
			}
			else
			{
				final Intent i = new Intent(this, SetupUserDetails.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				Request meRequest = Request.newMeRequest(session,
						new GraphUserCallback() {

							@Override
							public void onCompleted(GraphUser user,
									Response response)
							{
								if (user != null)
								{
									// Display the parsed user info

									// Insert value below

									// FACEBOOK ID (used for showing profile
									// picture)
									userSP.edit()
											.putString("userid", user.getId())
											.commit();

									userSP.edit()
											.putString("name",
													user.getName().toString())
											.commit();

									userSP.edit()
											.putString("email",
													getEmail(getBaseContext()))
											.commit();

									Calendar c = Calendar.getInstance();

									userSP.edit()
											.putInt("age",
													c.get(Calendar.YEAR)
															- Integer
																	.parseInt(user
																			.getBirthday()
																			.substring(
																					user.getBirthday()
																							.length() - 4)))
											.commit();

									String gender = user.asMap().get("gender")
											.toString();

									if (gender.equalsIgnoreCase("male"))
										gender = "Male";
									else if (gender.equalsIgnoreCase("female"))
										gender = "Female";

									userSP.edit().putString("gender", gender)
											.commit();

									if (userSP.getBoolean("userCreated", false) == false)
										startActivity(i);

								}
							}
						});

				meRequest.executeAsync();
				this.finish();
			}

		}
		else
		{
			// otherwise show the splash and ask person to login
			showFragment(LOGIN, false);
		}
	}

	// prepare option menu display
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		// only add the menu when the selection fragment is showing
		// if (fragments[SELECTION].isVisible() ||
		// fragments[LOGIN].isVisible())
		// {
		// if (menu.size() == 0)
		// {
		menu.clear();
		logOut = menu.add(R.string.logOut);

		// }
		return true;
		// }
		/* else { menu.clear(); settings = null; } */
		// return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.equals(logOut))
		{
			showFragment(SETTINGS, true);

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	//Get gmail
	public String getEmail(Context context)
	{
		AccountManager accountManager = AccountManager.get(context);
		Account account = getAccount(accountManager);

		if (account == null)
		{
			return null;
		}
		else
		{
			return account.name;
		}
	}

	//Get Google account
	private Account getAccount(AccountManager accountManager)
	{
		Account[] accounts = accountManager.getAccountsByType("com.google");
		Account account;
		if (accounts.length > 0)
		{
			account = accounts[0];
		}
		else
		{
			account = null;
		}
		return account;
	}

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		finish();
	}

}
