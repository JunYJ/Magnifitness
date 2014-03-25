package com.madmonkey.magnifitness;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madmonkey.magnifitness.util.DatabaseHandler;
import com.madmonkey.magnifitnessClass.User;

public class SetupUserDetails extends Activity implements
		OnCheckedChangeListener
{
	TextView			heightTV, weightTV;
	EditText			name, email, age, weightET, heightET;
	RadioGroup			genderRG;
	NumberPicker		weightPicker, heightPicker;
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
		initialization();

		loadFromPreferences();
		weightET.requestFocus();

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

	private void initialization()
	{
		// find View
		
		//TextView
		heightTV = (TextView) findViewById(R.id.heightTV);
		weightTV = (TextView) findViewById(R.id.weightTV);

		//EditText
		name = (EditText) findViewById(R.id.username);
		email = (EditText) findViewById(R.id.emailET);
		age = (EditText) findViewById(R.id.ageET);
		weightET = (EditText) findViewById(R.id.weightET);
		heightET = (EditText) findViewById(R.id.heightET);

		//RadioGroup
		genderRG = (RadioGroup) findViewById(R.id.genderRG);
		maleRB = (RadioButton) findViewById(R.id.rbMale);
		femaleRB = (RadioButton) findViewById(R.id.rbFemale);

		//Spinner
		lvOfActiveness = (Spinner) findViewById(R.id.lvOfActivenssSpinner);
		
		//Button
		okBtn = (Button) findViewById(R.id.okBtn);

		//Weight & Height Layout
		LayoutInflater inflater = getLayoutInflater();
		View selectWeightLayout = inflater
				.inflate(R.layout.select_weight, null);
		View selectHeightLayout = inflater
				.inflate(R.layout.select_height, null);
		
		//Weight & Height Picker
		weightPicker = (NumberPicker) selectWeightLayout
				.findViewById(R.id.weightPicker);

		heightPicker = (NumberPicker) selectHeightLayout
				.findViewById(R.id.heightPicker);
		
		weightET.setInputType(InputType.TYPE_NULL);
		heightET.setInputType(InputType.TYPE_NULL);

		genderRG.setOnCheckedChangeListener(this);
		genderRG.check(R.id.rbMale);

		user = new User();
		userSP = getSharedPreferences(FacebookLogin.filename, 0);
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
		// Insert value below

		//Name
		userSP.edit().putString("name", name.getText().toString()).commit();

		//Email validation
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email.getText().toString());

		if (matcher.matches())
		{
			userSP.edit().putString("email", email.getText().toString())
					.commit();
		}
		else
		{
			Toast.makeText(getApplication(), "Invalid email", Toast.LENGTH_LONG)
					.show();
		}

		//Gender
		userSP.edit().putString("gender", gender).commit();
		
		//Age
		userSP.edit().putInt("age", Integer.parseInt(age.getText().toString()))
				.commit();
		
		//Level of Activeness
		userSP.edit()
				.putInt("lvOfActiveness",
						lvOfActiveness.getSelectedItemPosition()).commit();
		
		//Flag for existence of user account
		userSP.edit().putBoolean("userCreated", userCreated).commit();

		//Weight
		userSP.edit()
				.putInt("weight",
						Integer.parseInt(weightET.getEditableText().toString()))
				.commit();
		user.setCurrentWeight(Integer.parseInt(weightET.getEditableText()
				.toString()));

		//Height
		userSP.edit()
				.putInt("height",
						Integer.parseInt(heightET.getEditableText().toString()))
				.commit();
		user.setHeight(Integer.parseInt(heightET.getEditableText().toString()));

		//BMI & BMR
		DecimalFormat df = new DecimalFormat("#.##");
		userSP.edit().putString("bmi", df.format(user.getBmi()) + "").commit();
		userSP.edit().putString("bmr", df.format(user.getBmr()) + "").commit();
	}

	private void loadFromPreferences()
	{
		name.setText(userSP.getString("name", ""));
		email.setText(userSP.getString("email", ""));
		weightET.setText(userSP.getInt("weight", 0) + "");
		heightET.setText(userSP.getInt("height", 0) + "");
		if (userSP.getString("gender", "").equalsIgnoreCase("male"))
			maleRB.setChecked(true);
		else if (userSP.getString("gender", "").equalsIgnoreCase("female"))
			femaleRB.setChecked(true);

		age.setText(userSP.getInt("age", 0) + "");
		lvOfActiveness.setSelection(userSP.getInt("lvOfActiveness", 0));
	}

	//Weight picker alert dialog
	public void selectWeight(View v)
	{
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);

		LayoutInflater inflater = getLayoutInflater();
		View selectWeightLayout = inflater
				.inflate(R.layout.select_weight, null);
		helpBuilder.setView(selectWeightLayout);

		weightPicker = (NumberPicker) selectWeightLayout
				.findViewById(R.id.weightPicker);
		weightPicker.setMaxValue(250);
		weightPicker.setMinValue(30);
		weightPicker.setValue(userSP.getInt("weight", 50));

		helpBuilder.setPositiveButton("Confirm",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which)
					{
						userSP.edit().putInt("weight", weightPicker.getValue())
								.commit();

						weightET.setText(weightPicker.getValue() + "");
						heightET.requestFocus();
					}
				});

		helpBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{

					}
				});

		// Remember, create doesn't show the dialog
		AlertDialog helpDialog = helpBuilder.create();

		helpDialog.show();
	}

	//Height picker alert dialog
	public void selectHeight(View v)
	{
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);

		LayoutInflater inflater = getLayoutInflater();
		View selectHeightLayout = inflater
				.inflate(R.layout.select_height, null);
		helpBuilder.setView(selectHeightLayout);

		heightPicker = (NumberPicker) selectHeightLayout
				.findViewById(R.id.heightPicker);
		heightPicker.setMaxValue(250);
		heightPicker.setMinValue(130);
		heightPicker.setValue(userSP.getInt("height", 130));

		helpBuilder.setPositiveButton("Confirm",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which)
					{
						userSP.edit().putInt("height", heightPicker.getValue())
								.commit();

						heightET.setText(heightPicker.getValue() + "");
					}
				});

		helpBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{

					}
				});

		// Remember, create doesn't show the dialog
		AlertDialog helpDialog = helpBuilder.create();

		helpDialog.show();
	}

	//Confirm button
	public void confirmSetup(View v)
	{
		ageString = age.getText().toString();

		ageInt = Integer.parseInt(ageString);

		int weight = Integer.parseInt(weightET.getText().toString());// weightPicker.getValue();

		int height = Integer.parseInt(heightET.getText().toString());// heightPicker.getValue();

		//Weight & Height validation
		if (weight != 0 && height != 0)
		{
			user.setUser(name.getText().toString(), ageInt, email.getText()
					.toString(), gender, weightPicker.getValue(), heightPicker
					.getValue(), lvOfActiveness.getSelectedItemPosition());
			userCreated = true;
			saveInformation();

			nextActivity = new Intent(SetupUserDetails.this, Home.class);
			// nextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			nextActivity.putExtra("userObject", user);

			//First time run (create account, not editing)
			if (userSP.getBoolean("edittingProfile", false) == false)
				startActivity(nextActivity);
			
			//Edit profile
			else
			{
				Log.i("PROFILE EDITING (SETUP_USER_DETAILS)", "done editing");

				Intent returnIntent = new Intent();
				returnIntent.putExtra("name", user.getName());
				returnIntent.putExtra("gender", user.getGender());
				returnIntent.putExtra("age", user.getAge());
				returnIntent.putExtra("weight", user.getCurrentWeight());
				returnIntent.putExtra("height", user.getHeight());
				Log.e("WEIGHT", weightET.getEditableText().toString());
				Log.e("HEIGHT", heightET.getEditableText().toString());
				returnIntent.putExtra("bmi", user.getBmi());
				returnIntent.putExtra("bmr", user.getBmr());
				Log.e("BMI", user.getBmi() + "");
				Log.e("BMR", user.getBmr() + "");
				returnIntent.putExtra("email", user.getEmail());
				setResult(RESULT_OK, returnIntent);
			}

			this.finish();
		}
		//Show Toast message
		else
		{
			if (weight == 0)
				Toast.makeText(getBaseContext(), "Please choose a weight",
						Toast.LENGTH_SHORT).show();
			else if (height == 0)
				Toast.makeText(getBaseContext(), "Please choose a height",
						Toast.LENGTH_SHORT).show();
		}
	}
}
