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
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

public class FacebookLogin extends FragmentActivity {
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
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private MenuItem logOut;

	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
	private static final String TAG = "Facebook Login";
	Button setupUserBtn;
	SharedPreferences userSP;
	public static String filename = "com.madmonkey.magnifitness.SharedPref";
	boolean userCreated;

	// hide fragments initially in onCreate
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		uiHelper = new UiLifecycleHelper(this, callBack);
		uiHelper.onCreate(savedInstanceState);
		setContentView(R.layout.facebook_login);

		FragmentManager fm = getSupportFragmentManager();
		fragments[LOGIN] = fm.findFragmentById(R.id.loginFragment);
		fragments[SELECTION] = fm.findFragmentById(R.id.selectionFragment);
		fragments[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);

		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++) {
			transaction.hide(fragments[i]);
		}
		transaction.commit();
		userSP = getSharedPreferences(FacebookLogin.filename, 0);
		userCreated = userSP.getBoolean("userCreated", false);

		// final ActionBar actionBar = getActionBar();
		// actionBar.setDisplayHomeAsUpEnabled(true);
	}

	// responsible for showing a given fragment & hiding all other fragments
	private void showFragment(int fragmentIndex, boolean addToBackStack) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		for (int i = 0; i < fragments.length; i++) {
			if (i == fragmentIndex) {
				transaction.show(fragments[i]);
			} else {
				transaction.hide(fragments[i]);
			}
		}
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		isResumed = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
		isResumed = false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	// called due to state changes
	// shows relevant fragment based on user's authenticated state
	// fragment back stack is 1st cleared before the showFragment() is called
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		// Only make changes if the activity is visible
		if (isResumed) {
			android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

			// Get the number of entries in the back stack
			int backStackSize = manager.getBackStackEntryCount();

			// Clear the back stack
			for (int i = 0; i < backStackSize; i++) {
				manager.popBackStack();
			}

			if (state.isOpened()) {
				// If the session state is open --> show the authenticated
				// fragment
				Log.i(TAG, "Logged in...");
				showFragment(SELECTION, false);

				// Request user data and show the results
				Request.executeMeRequestAsync(session,
						new Request.GraphUserCallback() {
							@Override
							public void onCompleted(GraphUser user,
									Response response) {
								if (user != null) {
									// Display the parsed user info
									SelectionFragment.userInfo
											.setText(buildUserInfoDisplay(user));

									SharedPreferences shared = getSharedPreferences(
											FacebookLogin.filename,
											MODE_PRIVATE);
									SharedPreferences.Editor editor = shared
											.edit();
									// Insert value below
									editor.putString("name", user.getName()
											.toString());
									editor.putString("email",
											getEmail(getBaseContext()));
									// editor.putString("gender", gender);

									Calendar c = Calendar.getInstance();

									editor.putInt(
											"age",
											c.get(Calendar.YEAR)
													- Integer
															.parseInt(user
																	.getBirthday()
																	.substring(
																			user.getBirthday()
																					.length() - 4)));
									// commit changes to the SharedPref
									editor.commit();
								}
							}
						});
			}

			else if (state.isClosed()) {
				Log.i(TAG, "Logged out...");
				// If the session state is close --> show the login fragment
				showFragment(LOGIN, false);
			}
		}
	}

	// handle the case where fragments are newly instantiated
	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		Session session = Session.getActiveSession();
		// userCreated = userSP.getBoolean("userCreated", false);
		if (session != null && session.isOpened()) {
			// if the session is already open --> try to show the selection
			// fragment

			if (userCreated == true) {
				// userCreated = false;
				startActivity(new Intent(this, Home.class));
				finish();
			} else {
				showFragment(SELECTION, false);
				Request.executeMeRequestAsync(session,
						new Request.GraphUserCallback() {

							@Override
							public void onCompleted(GraphUser user,
									Response response) {
								if (user != null) {
									// Display the parsed user info
									SelectionFragment.userInfo
											.setText(buildUserInfoDisplay(user));
								}
							}
						});
			}

		} else {
			// otherwise show the splash and ask person to login
			showFragment(LOGIN, false);
		}
	}

	// prepare option menu display
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// only add the menu when the selection fragment is showing
		// if (fragments[SELECTION].isVisible() || fragments[LOGIN].isVisible())
		// {
		// if (menu.size() == 0)
		// {
		menu.clear();
		logOut = menu.add(R.string.logOut);

		// }
		return true;
		// }
		/*
		 * else { menu.clear(); settings = null; }
		 */
		// return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.equals(logOut)) {
			showFragment(SETTINGS, false);

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private String buildUserInfoDisplay(GraphUser user) {
		userSP.edit().putString("userid", user.getId()).commit();
		StringBuilder userInfo = new StringBuilder("");
		SelectionFragment.profilePictureView.setProfileId(user.getId());

		// Task.getProfilePic().setProfileId(user.getId());
		// USER NAME
		// Example: typed access (name)
		// - no special permissions required
		// userInfo.append(String.format("Name: %s\n\n", user.getName()));
		userInfo.append(String.format("Name: %s\n\n",
				userSP.getString("name", user.getName())));
		// Example: typed access (birthday)
		// - requires user_birthday permission
		// userInfo.append(String.format("Birthday: %s\n\n",
		// user.getBirthday()));

		// GENDER
		userInfo.append(String.format("Gender: %s\n\n",
				userSP.getString("gender", "Not mention")));

		// AGE
		Calendar c = Calendar.getInstance();

		userInfo.append(String.format(
				"Age: %s\n\n",
				c.get(Calendar.YEAR)
						- Integer.parseInt(user.getBirthday().substring(
								user.getBirthday().length() - 4))));

		// WEIGHT
		userInfo.append(String.format("Weight: %s kg \n\n",
				userSP.getInt("weight", 0)));
		// HEIGHT
		userInfo.append(String.format("Height: %s cm \n\n",
				userSP.getInt("height", 0)));
		// BMI
		userInfo.append(String.format("BMI: %s\n\n",
				userSP.getString("bmi", "")));
		// BMR
		userInfo.append(String.format("BMR: %s\n\n",
				userSP.getString("bmr", "")));

		// EMAIL (sharedPreference)
		userInfo.append(String.format("Email: %s\n\n",
				userSP.getString("email", "")));
		// EMAIL (through facebook sdk)
		// String email = getEmail(this);
		// System.out.println("Android registered Email " + email);

		/*
		 * String test = user.getProperty("email").toString();
		 * System.out.println("TESTING " + test);
		 */

		/*
		 * userInfo.append(String.format("Facebook primary Email: %s\n\n",
		 * user.asMap() .get("email")));
		 * 
		 * userInfo.append(String.format("Android registered Email: %s\n\n",
		 * email));
		 */

		return userInfo.toString();
	}

	public String getEmail(Context context) {
		AccountManager accountManager = AccountManager.get(context);
		Account account = getAccount(accountManager);

		if (account == null) {
			return null;
		} else {
			return account.name;
		}
	}

	private Account getAccount(AccountManager accountManager) {
		Account[] accounts = accountManager.getAccountsByType("com.google");
		Account account;
		if (accounts.length > 0) {
			account = accounts[0];
		} else {
			account = null;
		}
		return account;
	}
}
