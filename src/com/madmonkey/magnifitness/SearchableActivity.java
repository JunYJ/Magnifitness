package com.madmonkey.magnifitness;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SearchableActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState)
		{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		
		
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      doMySearch(query);
	    }
		
		}

	private void doMySearch(String query)
		{
		
		Log.i("Search", "Searching for " + query);
		}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
		{
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("Check", "INTENT is Action_search");
		
		
		}




}
