package com.madmonkey.magnifitness;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

import com.madmonkey.magnifitnessClass.User;

public class SetupUserDetails extends Activity implements OnCheckedChangeListener
{
	EditText name, email, age;
	RadioGroup genderRG;
	NumberPicker  weightPicker, heightPicker;
	Spinner bmrSpinner;
	RadioButton maleRB, femaleRB;
	Button okBtn;
	User user;
	SharedPreferences userSP;
	String gender;
	String ageString;
	int ageInt; 
	
	boolean userCreated = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_detail_setup);
		getView();	
		user = new User();
		userSP = getSharedPreferences(FacebookLogin.filename, 0);
		//ERROR HERE
		loadFromPreferences();
		
		
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
				System.out.println("gender " + gender);
				System.out.println("age " + age.getText().toString());
				System.out.println("weight " + weightPicker.getValue());
				System.out.println("height " + heightPicker.getValue());
				
				ageString = age.getText().toString();
 
				ageInt = Integer.parseInt(ageString);				
				
				//setResult(RESULT_OK, returnIntent);
				user.setUser(name.getText().toString(), ageInt , email.getText().toString()
						, gender, weightPicker.getValue(), heightPicker.getValue());
				userCreated = true;
				saveInformation();
				
				//SelectionFragment.setupUserBtn.setVisibility(View.GONE);
				finish();
				startActivity(new Intent(SetupUserDetails.this, Home.class));
			}
			
		});
	}
	
	private void getView()
	{
		name = (EditText) findViewById(R.id.username);
		email = (EditText) findViewById(R.id.emailET);
		genderRG = (RadioGroup) findViewById(R.id.genderRG);
		maleRB = (RadioButton) findViewById(R.id.rbMale);
		femaleRB = (RadioButton) findViewById(R.id.rbFemale);
		age = (EditText) findViewById(R.id.ageET);
		weightPicker = (NumberPicker) findViewById(R.id.weightPicker);
		heightPicker = (NumberPicker) findViewById(R.id.heightPicker);
		bmrSpinner = (Spinner) findViewById(R.id.bmrSpinner);
		okBtn = (Button) findViewById(R.id.okBtn);
		
		weightPicker.setMaxValue(250);
		weightPicker.setMinValue(30);
		weightPicker.setValue(50);
		heightPicker.setMaxValue(250);
		heightPicker.setMinValue(130);
		heightPicker.setValue(150);
		
		genderRG.setOnCheckedChangeListener(this);
	}
	
	/**
	 * This method handle the RadioGroup
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		
		switch (checkedId) {
		case R.id.rbMale:
			gender = "Male";
			break;
		case R.id.rbFemale:
			gender = "Female";
			break;
		}
	}
	
	public void saveInformation() {
        SharedPreferences shared = getSharedPreferences(FacebookLogin.filename, MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        //Insert value below
        editor.putString("name", name.getText().toString());
        editor.putString("email", email.getText().toString());
        editor.putString("gender", gender);
        editor.putInt("age", Integer.parseInt(age.getText().toString()));
        editor.putInt("weight", weightPicker.getValue());
        editor.putInt("height", heightPicker.getValue());
        editor.putBoolean("userCreated", userCreated);
        //commit changes to the SharedPref
        editor.commit();
    }
	
	private void loadFromPreferences() {
		SharedPreferences shared = getSharedPreferences(FacebookLogin.filename, MODE_PRIVATE);
		name.setText(shared.getString("name", ""));
		email.setText(shared.getString("email", ""));
		if(shared.getString("gender", "").equalsIgnoreCase("male")) 
			maleRB.setChecked(true);
		else if(shared.getString("gender", "").equalsIgnoreCase("female"))
			femaleRB.setChecked(true);
		
		age.setText(shared.getInt("age", 0) + "");
		
		weightPicker.setValue(shared.getInt("weight", 50));
		heightPicker.setValue(shared.getInt("height", 150));
	}
	
	
	

}
