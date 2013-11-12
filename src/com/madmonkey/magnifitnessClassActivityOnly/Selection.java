package com.madmonkey.magnifitnessClassActivityOnly;

import com.madmonkey.magnifitness.R;
import com.madmonkey.magnifitness.SetupUserDetails;
import com.madmonkey.magnifitness.R.id;
import com.madmonkey.magnifitness.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Selection extends Activity 
{
	private static final String TAG = "SelectionFragment";
	protected static TextView userInfo;
	protected static Button setupUserBtn;
	Selection selection;
	//Set-up the view from the layout
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selection);
		Log.i(TAG, "SELECTION FRAGMENT");
		userInfo = (TextView) findViewById(R.id.txt);
		setupUserBtn = (Button) findViewById(R.id.setupUserBtn);
		selection = this;
		setupUserBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				startActivity(new Intent(selection, SetupUserDetails.class));
				
			}
		
		});
		
		//String name = getArguments().getString("name");
		//System.out.println("NAME: " + name);;
		/*userInfo.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				Bundle bundle = new Bundle();
				bundle.putInt("myData", x);
				Intent nextScreen = new Intent(getActivity(), SetupUserDetails.class);
				nextScreen.putExtras(bundle);
				startActivityForResult(nextScreen, 1);
				
			}
		});*/
		//String name = getArguments().getString("name");
		//System.out.println("NAME: " + name);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK)
		{
			System.out.println("Intent: " + data.getIntExtra("name", 0));
			System.out.println("Bundle: " + data.getBundleExtra("name"));
		}
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		//super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.home, menu);
	    //menu.add(Menu.NONE, R.id.userSettingsFragment, Menu.NONE, "Log Out");
	    //menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete");
	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
        {
            case R.id.Settings:
            	//startActivity(selection,com.facebook.LoginActivity.class);
            	return true;
            case R.id.LogOut:

            	return true;
            default:
            	return super.onOptionsItemSelected(item);
        }
	}*/
}
