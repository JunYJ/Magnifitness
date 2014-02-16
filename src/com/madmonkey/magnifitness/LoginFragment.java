package com.madmonkey.magnifitness;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class LoginFragment extends Fragment {
//
//	private static final String		TAG			= "LoginFragment";
//
//	private Session.StatusCallback	callback	= new Session.StatusCallback()
//													{
//														@Override
//														public void call(Session session, SessionState state, Exception exception)
//															{
//															onSessionStateChange(session, state, exception);
//															}
//													};
//
//	private UiLifecycleHelper		uiHelper;
//	private TextView				username, birthday, email;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState)
//		{
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		uiHelper = new UiLifecycleHelper(getActivity(), callback);
//		uiHelper.onCreate(savedInstanceState);
//
//		}
//
//	@Override
//	public void onResume()
//		{
//		super.onResume();
//		// For scenarios where the main activity is launched and user
//		// session is not null, the session state change notification
//		// may not be triggered. Trigger it if it's open/closed.
//		Session session = Session.getActiveSession();
//		if (session != null && (session.isOpened() || session.isClosed()))
//			{
//			onSessionStateChange(session, session.getState(), null);
//			}
//
//		uiHelper.onResume();
//		}
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data)
//		{
//		super.onActivityResult(requestCode, resultCode, data);
//		uiHelper.onActivityResult(requestCode, resultCode, data);
//		}
//
//	@Override
//	public void onPause()
//		{
//		super.onPause();
//		uiHelper.onPause();
//		}
//
//	@Override
//	public void onDestroy()
//		{
//		super.onDestroy();
//		uiHelper.onDestroy();
//		}
//
//	@Override
//	public void onSaveInstanceState(Bundle outState)
//		{
//		super.onSaveInstanceState(outState);
//		uiHelper.onSaveInstanceState(outState);
//		}
//
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
		View view = inflater.inflate(R.layout.login, container, false);

//		LoginButton loginBtn = (LoginButton) view.findViewById(R.id.login_button);
//		username = (TextView) view.findViewById(R.id.user_nameTV);
//		birthday = (TextView) view.findViewById(R.id.birthdayTV);
//		email = (TextView) view.findViewById(R.id.emailTV);

//		loginBtn.setFragment(this);
//		loginBtn.setReadPermissions(Arrays.asList("name", "gender", "user_birthday", "email", "user_location", "user_likes"));

		return view;
		}
//
//	private void onSessionStateChange(Session session, SessionState state, Exception exception)
//		{
//		if (state.isOpened())
//			{
//			Log.i(TAG, "Logged in...");
//			username.setVisibility(View.VISIBLE);
//			birthday.setVisibility(View.VISIBLE);
//			email.setVisibility(View.VISIBLE);
//
//			Request meRequest = Request.newMeRequest(session, new GraphUserCallback()
//				{
//
//					@Override
//					public void onCompleted(GraphUser user, Response response)
//						{
//
//						if (user != null)
//							{
//							username.setText(buildUserInfoDisplay(user));
//							birthday.setText(buildUserInfoDisplay(user));
//							}
//						}
//				});
//			
//			
//
//			}
//		else if (state.isClosed())
//			{
//			Log.i(TAG, "Logged out...");
//			username.setVisibility(View.INVISIBLE);
//			birthday.setVisibility(View.INVISIBLE);
//			email.setVisibility(View.INVISIBLE);
//			}
//		}
//
//	private String buildUserInfoDisplay(GraphUser user)
//		{
//		StringBuilder userInfo = new StringBuilder("");
//
//		// Example: typed access (name)
//		// - no special permissions required
//		userInfo.append(String.format("Name: %s\n\n", user.getName()));
//
//		// Example: typed access (birthday)
//		// - requires user_birthday permission
//		userInfo.append(String.format("Birthday: %s\n\n", user.getBirthday()));
//
//		// Example: partially typed access, to location field,
//		// name key (location)
//		// - requires user_location permission
//		userInfo.append(String.format("Location: %s\n\n", user.getLocation().getProperty("name")));
//
//		// Example: access via property name (locale)
//		// - no special permissions required
//		userInfo.append(String.format("Locale: %s\n\n", user.getProperty("locale")));
//
//		// Example: access via key for array (languages)
//		// - requires user_likes permission
//		// Get a list of languages from an interface that
//		// extends the GraphUser interface and that returns
//		// a GraphObject list of MyGraphLanguage objects.
//		GraphObjectList<MyGraphLanguage> languages = 
//		    (user.cast(MyGraphUser.class)).getLanguages();
//		if (languages.size() > 0) {
//		    ArrayList<String> languageNames = new ArrayList<String> ();
//		    // Iterate through the list of languages
//		    for (MyGraphLanguage language : languages) {
//		        // Add the language name to a list. Use the name
//		        // getter method to get access to the name field.
//		        languageNames.add(language.getName());
//		    }                     
//		                            
//		    userInfo.append(String.format("Languages: %s\n\n",
//		        languageNames.toString()));
//		}
//		
//
//		return userInfo.toString();
//		}
//
//	private interface MyGraphLanguage extends GraphObject {
//		// Getter for the ID field
//		String getId();
//
//		// Getter for the Name field
//		String getName();
//	}
//	
//	private interface MyGraphUser extends GraphUser {
//    // Create a setter to enable easy extraction of the languages field
//    GraphObjectList<MyGraphLanguage> getLanguages();
//}

}
