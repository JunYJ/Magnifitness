package com.madmonkey.magnifitnessClassActivityOnly;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.madmonkey.magnifitness.R;
import com.madmonkey.magnifitness.R.id;
import com.madmonkey.magnifitness.R.layout;
import com.madmonkey.magnifitness.R.menu;

public class Login extends Activity
{
	public static boolean isResumed = false; // flag to indicate a visible
												// activity
	private UiLifecycleHelper uiHelper;
	Login login;
	// track the session & trigger a session state change listener
	// Session.StatusCallback() overrides call() & invokes
	// onSessionStateChange()
	private Session.StatusCallback callBack = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception)
		{
			onSessionStateChange(session, state, exception);
		}
	};

	private static final String TAG = "Facebook Login";
	Button setupUserBtn;
	public static String filename = "com.madmonkey.magnifitness.SharedPref";

	private MenuItem settings;
	// hide fragments initially in onCreate
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		login = this;
		uiHelper = new UiLifecycleHelper(this, callBack);
		uiHelper.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		setupUserBtn = (Button) findViewById (R.id.setupUserBtn);
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

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception)
	{
		/*SharedPreferences shared  = getSharedPreferences(filename, MODE_PRIVATE);
		boolean isFirstRun = shared.getBoolean("isFirstRun", true);
		*/
		// Only make changes if the activity is visible
		if (isResumed)
		{
			if (state.isOpened())
			{
				// If the session state is open --> show the authenticated
				// fragment
				Log.i(TAG, "Logged in...");
				//showFragment(SELECTION, false);
				startActivity(new Intent(login, Selection.class));
				
				// Request user data and show the results
				Request.executeMeRequestAsync(session,
						new Request.GraphUserCallback() {
							@Override
							public void onCompleted(GraphUser user,
									Response response)
							{
								if (user != null)
								{
									// Display the parsed user info
									Selection.userInfo
											.setText(buildUserInfoDisplay(user));
								}
							}
						});
			}

			else if (state.isClosed())
			{
				Log.i(TAG, "Logged out...");
				// If the session state is close --> show the login fragment
				//startActivity(new Intent(login, Login.class));
			}
		}
		
		if(!isResumed)
		{
			if(session != null && session.isOpened())
			{
				startActivity(new Intent(login, Selection.class));
				
				// Request user data and show the results
				Request.executeMeRequestAsync(session,
						new Request.GraphUserCallback() {
							@Override
							public void onCompleted(GraphUser user,
									Response response)
							{
								if (user != null)
								{
									// Display the parsed user info
									Selection.userInfo
											.setText(buildUserInfoDisplay(user));
								}
							}
						});
			}
		}
	}
	
	private String buildUserInfoDisplay(GraphUser user)
	{
		StringBuilder userInfo = new StringBuilder("");

		// Example: typed access (name)
		// - no special permissions required
		userInfo.append(String.format("Name: %s\n\n", user.getName()));

		// Example: typed access (birthday)
		// - requires user_birthday permission
		userInfo.append(String.format("Birthday: %s\n\n", user.getBirthday()));

		String email = getEmail(this);
		System.out.println("Android registered Email " + email);
	
		/*String test = user.getProperty("email").toString();
		System.out.println("TESTING " + test);*/

		userInfo.append(String.format("Facebook primary Email: %s\n\n", user.asMap()
				.get("email")));

		userInfo.append(String.format("Android registered Email: %s\n\n", email));

		return userInfo.toString();
	}

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
	
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		// only add the menu when the selection fragment is showing
		//if (fragments[SELECTION].isVisible() || fragments[LOGIN].isVisible())
		//{
			/*if (menu.size() == 0)
			{
				settings = menu.add(R.string.settings);
			}
			return true;
		//}
		/*
		 * else { menu.clear(); settings = null; }
		 */
		//return false;
		menu.clear();
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.equals(settings))
		{
			//showFragment(SETTINGS, false);
			//startActivityFromFragment(com.facebook.widget.UserSettingsFragment, null, 0);
			System.out.println("SETTINGS PRESSED!");
			return true;
		}
		return false;
	}
	
}
