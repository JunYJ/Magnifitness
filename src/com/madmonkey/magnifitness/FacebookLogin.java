package com.madmonkey.magnifitness;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

public class FacebookLogin extends FragmentActivity
{
	// Fragment index
	private static final int LOGIN = 0;
	private static final int SELECTION = 1;
	private static final int SETTINGS = 2;

	private static final int FRAGMENT_COUNT = SETTINGS + 1;

	public static boolean isResumed = false; // flag to indicate a visible
												// activity
	private UiLifecycleHelper uiHelper;

	// track the session & trigger a session state change listener
	// Session.StatusCallback() overrides call() & invokes
	// onSessionStateChange()
	private Session.StatusCallback callBack = new Session.StatusCallback() {
		public void call(Session session, SessionState state,
				Exception exception)
		{
			onSessionStateChange(session, state, exception);
		}
	};

	private MenuItem settings;

	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
	private static final String TAG = "Facebook Login";
	Button setupUserBtn;

	// hide fragments initially in onCreate
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		uiHelper = new UiLifecycleHelper(this, callBack);
		uiHelper.onCreate(savedInstanceState);
		setContentView(R.layout.facebook_login);

		FragmentManager fm = getSupportFragmentManager();
		fragments[LOGIN] = fm.findFragmentById(R.id.loginFragment);
		fragments[SELECTION] = fm.findFragmentById(R.id.selectionFragment);
		fragments[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);

		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++)
		{
			transaction.hide(fragments[i]);
		}
		transaction.commit();
		setupUserBtn = SelectionFragment.setupUserBtn;
	}

	// responsible for showing a given fragment & hiding all other fragments
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

	public void onResume()
	{
		super.onResume();
		uiHelper.onResume();
		isResumed = true;
	}

	public void onPause()
	{
		super.onPause();
		uiHelper.onPause();
		isResumed = false;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	public void onDestroy()
	{
		super.onDestroy();
		uiHelper.onDestroy();
	}

	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	// called due to state changes
	// shows relevant fragment based on user's authenticated state
	// fragment back stack is 1st cleared before the showFragment() is called
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception)
	{
		// String[] PERMISSION_ARRAY_READ = {"email","user_birthday"};
		// List<String> PERMISSION_LIST = Arrays.asList(PERMISSION_ARRAY_READ);
		/*
		 * Session.NewPermissionsRequest request = new
		 * Session.NewPermissionsRequest(FacebookLogin.this, PERMISSION_LIST);
		 * session.requestNewReadPermissions(request);
		 */
		// Only make changes if the activity is visible
		if (isResumed)
		{
			FragmentManager manager = getSupportFragmentManager();

			// Get the number of entries in the back stack
			int backStackSize = manager.getBackStackEntryCount();

			// Clear the back stack
			for (int i = 0; i < backStackSize; i++)
			{
				manager.popBackStack();
			}

			// session.openForRead(new
			// Session.OpenRequest(getParent()).setPermissions(PERMISSION_LIST).setCallback(callBack));

			if (state.isOpened())
			{
				// If the session state is open --> show the authenticated
				// fragment
				Log.i(TAG, "Logged in...");
				showFragment(SELECTION, false);

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
									SelectionFragment.userInfo
											.setText(buildUserInfoDisplay(user));
								}
							}
						});
				
				setupUserBtn.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						
						startActivity(new Intent(FacebookLogin.this, SetupUserDetails.class));
						
					}
				});
			}

			else if (state.isClosed())
			{
				Log.i(TAG, "Logged out...");
				// If the session state is close --> show the login fragment
				showFragment(LOGIN, false);
			}
		}
	}

	// handle the case where fragments are newly instantiated
	protected void onResumeFragments()
	{
		super.onResumeFragments();
		Session session = Session.getActiveSession();

		if (session != null && session.isOpened())
		{
			// if the session is already open --> try to show the selection
			// fragment
			showFragment(SELECTION, false);
			Request.executeMeRequestAsync(session,
					new Request.GraphUserCallback() {

						@Override
						public void onCompleted(GraphUser user,
								Response response)
						{
							if (user != null)
							{
								// Display the parsed user info
								SelectionFragment.userInfo
										.setText(buildUserInfoDisplay(user));
							}
						}
					});
			
			setupUserBtn.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					
					startActivity(new Intent(FacebookLogin.this, SetupUserDetails.class));
					
				}
			});
		}
		else
		{
			// otherwise show the splash and ask person to login
			showFragment(LOGIN, false);
		}
	}

	// prepare option menu display
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		// only add the menu when the selection fragment is showing
		if (fragments[SELECTION].isVisible() || fragments[LOGIN].isVisible())
		{
			if (menu.size() == 0)
			{
				settings = menu.add(R.string.settings);
			}
			return true;
		}
		/*
		 * else { menu.clear(); settings = null; }
		 */
		return false;
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.equals(settings))
		{
			showFragment(SETTINGS, false);
			return true;
		}
		return false;
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
	
		String test = user.getProperty("email").toString();
		System.out.println("TESTING " + test);

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
}
