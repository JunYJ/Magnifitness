package com.madmonkey.magnifitness;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.madmonkey.magnifitnessClass.User;

public class SetupUserDetails extends Activity
{
	EditText name, email, gender, age;
	NumberPicker  weightPicker, heightPicker;
	Spinner bmrSpinner;
	Button okBtn;
	User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_detail_setup);
		getView();	
		user = new User();
		//get data from SelectionFragment
		/*Bundle bundle = getIntent().getExtras();
		int value = bundle.getInt("myData");
		Log.v("TEST", ""+value);
		System.out.println("TEST " + value);
		
		bundle.putString("name", name.getText().toString());
		bundle.putString("email", email.getText().toString());
		bundle.putString("gender", gender.getText().toString());
		
		final Intent returnIntent = getIntent();
		returnIntent.putExtra("name", name.getText().toString());
		returnIntent.putExtra("email", email.getText().toString());
		returnIntent.putExtra("gender", gender.getText().toString());
		returnIntent.putExtra("age", agePicker.getValue());
		returnIntent.putExtra("weight", weightPicker.getValue());
		returnIntent.putExtra("height", heightPicker.getValue());
		returnIntent.putExtras(bundle);
		returnIntent.putExtras(getIntent());*/
		
		okBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				System.out.println("name " + name.getText().toString());
				System.out.println("email " + email.getText().toString());
				System.out.println("gender " + gender.getText().toString());
				System.out.println("age " + age.getText().toString());
				System.out.println("weight " + weightPicker.getValue());
				System.out.println("height " + heightPicker.getValue());
				
				int ageInt = Integer.parseInt(age.getText().toString());
				//setResult(RESULT_OK, returnIntent);
				user.setUser(name.getText().toString(), ageInt , email.getText().toString()
						, gender.getText().toString(), weightPicker.getValue(), heightPicker.getValue());
				finish();
			}
			
		});
	}
	
	private void getView()
	{
		name = (EditText) findViewById(R.id.username);
		email = (EditText) findViewById(R.id.email);
		gender = (EditText) findViewById(R.id.gender);
		//stopped here
		age = (EditText) findViewById(R.id.agePicker);
		weightPicker = (NumberPicker) findViewById(R.id.weightPicker);
		heightPicker = (NumberPicker) findViewById(R.id.heightPicker);
		bmrSpinner = (Spinner) findViewById(R.id.bmrSpinner);
		okBtn = (Button) findViewById(R.id.okBtn);
		
		
		weightPicker.setMaxValue(250);
		weightPicker.setMinValue(30);
		heightPicker.setMaxValue(250);
		heightPicker.setMinValue(130);
	}

}
