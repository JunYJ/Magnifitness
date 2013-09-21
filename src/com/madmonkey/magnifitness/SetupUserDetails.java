package com.madmonkey.magnifitness;

import android.app.Activity;
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
	public static String filename = "com.madmonkey.magnifitness.SharedPref";
	String gender;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_detail_setup);
		getView();	
		user = new User();
		userSP = getSharedPreferences(filename, 0);
		//ERROR HERE
		//loadFromPreferences();
		
		
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
				
				int ageInt = Integer.parseInt(age.getText().toString());
				//setResult(RESULT_OK, returnIntent);
				user.setUser(name.getText().toString(), ageInt , email.getText().toString()
						, gender, weightPicker.getValue(), heightPicker.getValue());
				saveInformation();
				finish();
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
		heightPicker.setMaxValue(250);
		heightPicker.setMinValue(130);
		
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
        SharedPreferences shared = getSharedPreferences(filename, MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        //Insert value below
        editor.putString("name", name.getText().toString());
        editor.putString("email", email.getText().toString());
        editor.putString("gender", gender);
        editor.putInt("age", Integer.parseInt(age.getText().toString()));
        editor.putInt("weight", weightPicker.getValue());
        editor.putInt("height", heightPicker.getValue());
        //commit changes to the SharedPref
        editor.commit();
    }
	
	private void loadFromPreferences() {
		SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
		name.setText(sharedPref.getString("name", ""));
		email.setText(sharedPref.getString("email", ""));
		if(sharedPref.getString("gender", "").equalsIgnoreCase("male")) 
			maleRB.setChecked(true);
		else
			femaleRB.setChecked(true);
		age.setText(sharedPref.getInt("age", 0));
		weightPicker.setValue(sharedPref.getInt("weight", 50));
		heightPicker.setValue(sharedPref.getInt("height", 150));
	}
	
	
	

}
