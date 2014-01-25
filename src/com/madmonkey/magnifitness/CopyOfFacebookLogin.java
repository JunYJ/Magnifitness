package com.madmonkey.magnifitness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class CopyOfFacebookLogin extends FragmentActivity {
	
	private LoginFragment loginFragment;
	

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    if (savedInstanceState == null) {
	        // Add the fragment on initial activity setup
	        loginFragment = new LoginFragment();
	        getSupportFragmentManager()
	        .beginTransaction()
	        .add(android.R.id.content, loginFragment)
	        .commit();
	    } else {
	        // Or set the fragment from restored state info
	        loginFragment = (LoginFragment) getSupportFragmentManager()
	        .findFragmentById(android.R.id.content);
	    }
	}



	@Override
	protected void onActivityResult(int reqCode, int resultCode, Intent data)
		{
		// TODO Auto-generated method stub
		super.onActivityResult(reqCode, resultCode, data);
		if(resultCode == RESULT_OK)
			startActivity(new Intent(getApplication(), Home.class));
			finish();
		}


	
}
