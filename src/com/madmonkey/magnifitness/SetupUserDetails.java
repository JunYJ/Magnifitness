package com.madmonkey.magnifitness;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.madmonkey.magnifitness.util.DatabaseHandler;
import com.madmonkey.magnifitnessClass.User;

public class SetupUserDetails extends Activity implements
		OnCheckedChangeListener
{
	EditText			name, email, age;
	RadioGroup			genderRG;
	NumberPicker		weightPicker, heightPicker, idealWeightPicker;
	Spinner				lvOfActiveness;
	RadioButton			maleRB, femaleRB;
	Button				okBtn;
	User				user;
	SharedPreferences	userSP;
	String				gender;
	String				ageString;
	int					ageInt;
	int					levelOfActiveness;
	Intent				nextActivity;

	boolean				userCreated	= false;
	DatabaseHandler		db;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_detail_setup);
		getView();
		user = new User();
		userSP = getSharedPreferences(FacebookLogin.filename, 0);
		// ERROR HERE
		loadFromPreferences();

		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				ageString = age.getText().toString();

				ageInt = Integer.parseInt(ageString);

				user.setUser(name.getText().toString(), ageInt, email.getText()
						.toString(), gender, weightPicker.getValue(),
						heightPicker.getValue(),
						lvOfActiveness.getSelectedItemPosition());
				userCreated = true;
				saveInformation();

				finish();

				nextActivity = new Intent(SetupUserDetails.this, Home.class);
				nextActivity.putExtra("userObject", user);
				startActivity(nextActivity);
				finish();
			}

		});

		lvOfActiveness
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id)
					{
						levelOfActiveness = pos;

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0)
					{

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
		idealWeightPicker = (NumberPicker) findViewById(R.id.idealWeightPicker);
		lvOfActiveness = (Spinner) findViewById(R.id.lvOfActivenssSpinner);
		okBtn = (Button) findViewById(R.id.okBtn);

		weightPicker.setMaxValue(250);
		weightPicker.setMinValue(30);
		weightPicker.setValue(50);

		heightPicker.setMaxValue(250);
		heightPicker.setMinValue(130);
		heightPicker.setValue(150);

		idealWeightPicker.setMaxValue(250);
		idealWeightPicker.setMinValue(30);
		idealWeightPicker.setValue(50);

		genderRG.setOnCheckedChangeListener(this);
		genderRG.check(R.id.rbMale);
	}

	/** This method handle the RadioGroup */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{

		switch (checkedId)
		{
			case R.id.rbMale:
				gender = "Male";
				break;
			case R.id.rbFemale:
				gender = "Female";
				break;
			default:
				gender = "";
				break;
		}
	}

	public void saveInformation()
	{
		SharedPreferences shared = getSharedPreferences(FacebookLogin.filename,
				MODE_PRIVATE);
		SharedPreferences.Editor editor = shared.edit();
		// Insert value below
		editor.putString("name", name.getText().toString());

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email.getText().toString());

		if (matcher.matches())
		{
			editor.putString("email", email.getText().toString());
		}
		else
		{
			Toast.makeText(getApplication(), "Invalid email", Toast.LENGTH_LONG)
					.show();
		}

		editor.putString("gender", gender);
		editor.putInt("age", Integer.parseInt(age.getText().toString()));
		editor.putInt("lvOfActiveness",
				lvOfActiveness.getSelectedItemPosition());
		editor.putInt("weight", weightPicker.getValue());
		editor.putInt("height", heightPicker.getValue());
		editor.putInt("idealWeight", idealWeightPicker.getValue());
		editor.putBoolean("userCreated", userCreated);

		DecimalFormat df = new DecimalFormat("#.##");
		editor.putString("bmi", df.format(user.getBmi()) + "");
		editor.putString("bmr", df.format(user.getBmr()) + "");

		// commit changes to the SharedPref
		editor.commit();
	}

	private void loadFromPreferences()
	{
		SharedPreferences shared = getSharedPreferences(FacebookLogin.filename,
				MODE_PRIVATE);
		name.setText(shared.getString("name", ""));
		email.setText(shared.getString("email", ""));
		if (shared.getString("gender", "").equalsIgnoreCase("male"))
			maleRB.setChecked(true);
		else if (shared.getString("gender", "").equalsIgnoreCase("female"))
			femaleRB.setChecked(true);

		age.setText(shared.getInt("age", 0) + "");
		lvOfActiveness.setSelection(shared.getInt("lvOfActiveness", 0));

		weightPicker.setValue(shared.getInt("weight", 50));
		heightPicker.setValue(shared.getInt("height", 150));
		idealWeightPicker.setValue(shared.getInt("idealWeight", 50));
	}

	
}
